import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.example.megaballs.MegaBall;
import org.example.megaballs.MegaBalls;
import org.example.megaballs.Numbers;
import org.example.numbers_hit.Count;
import org.example.numbers_hit.MegaHits;
import org.example.numbers_hit.NumberHits;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.sql.*;
import java.sql.Connection;

public class GrabDataFromWebsite {

	/**
	 * @param args
	 */

	private static final String FILE_NAME = "MegaballsWellFormat1.xml";
	private static final String FILE_NAME2 = "NumberHitsWellFormat1.xml";
	

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * try { URL google = new URL("http://www.google.com/"); BufferedReader
		 * in = new BufferedReader(new InputStreamReader(google.openStream()));
		 * String inputLine;
		 * 
		 * while ((inputLine = in.readLine()) != null) { // Process each line.
		 * System.out.println(inputLine); } in.close();
		 * 
		 * } catch (MalformedURLException me) { System.out.println(me);
		 * 
		 * } catch (IOException ioe) { System.out.println(ioe); } }//end main
		 */
		int count = 0;
		String numText = "";
		MegaBalls mbs = new MegaBalls();
		NumberHits nhs = new NumberHits();
		MegaHits mhs = new MegaHits();
		Count c = new Count();
		Document doc;
		String readFileToString;
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		HashMap<String, Integer> megaHm = new HashMap<String, Integer>();

		int hmcount = 1;
		// Java Object to XML
		try {
			// connection timeout set to 10 sec
			doc = Jsoup
					.connect(
							"http://www.megamillions.com/winning-numbers/last-25-drawings")
					.timeout(60 * 1000).get();

			Elements table = doc.getElementsByClass("winning-numbers-table");

			for (Element number : table) {
				for (Element tbody : table.select("tbody")) {
					for (Element tr : tbody.select("tr")) {

						// create new MegaBall and Numbers object each time read
						// the new date
						MegaBall mb = new MegaBall();
						Numbers numbers = new Numbers();
						// count++;
						// System.out.println("******** " + count +
						// " ********");
						String dateText = tr.getElementsByClass("dates").text();

						for (Element num : tr.getElementsByClass("number")) {
							numText = num.text();

							if (hm.containsKey(numText)) {
								hm.put(numText, (Integer) hm.get(numText) + 1);
							} else {
								hm.put(numText, hmcount);
							}
							numbers.getNumber().add(numText);
						}

						Elements megas = tr.getElementsByClass("mega");
						Element mega = megas.get(0);
						String megaText = mega.text();

						if (megaHm.containsKey(megaText)) {
							megaHm.put(megaText,
									(Integer) megaHm.get(megaText) + 1);
						} else {
							megaHm.put(megaText, hmcount);
						}

						// add to MegaBall

						mb.setDate(dateText);
						mb.setNumbers(numbers);
						mb.setMega(megaText);

						// add MegaBall<---mb to MegaBalls<----mbs
						mbs.getMegaBall().add(mb);

						// Insert to database (MegaMillions table)
						// InsertToMegamillions(dateText,numbers,megaText);

					}

					// convert mbs object to XML
					megaBallsToXML(mbs);

					// print number hashmap
					System.out.println();
					Set set = sortByValue(hm).entrySet();

					File fl = new File("combination.txt");
					FileWriter fw = new FileWriter(fl);
					// BufferedWriter writer give better performance
					BufferedWriter bw = new BufferedWriter(fw);

					String lineSeparator = System.getProperty("line.separator");

					List<Integer> vector = new ArrayList<Integer>();

					Iterator itr = set.iterator();
				
					while (itr.hasNext()) {
						Map.Entry m = (Map.Entry) itr.next();
					
						bw.append("Number: " + m.getKey() + " hit "
								+ m.getValue() + lineSeparator);
						
						nhs.setNumber(Integer.parseInt((String) m.getKey()));
						nhs.setHit((Integer)m.getValue());
						c.getNumberHits().add(nhs);
						System.out.println(c.getNumberHits().get(c.getNumberHits().size()-1).getNumber());
					
						if (((Integer) m.getValue() >= 2)
								&& ((Integer) m.getValue() <= 4)) {
							vector.add(Integer.parseInt((String) m.getKey()));
						}
					}

					// print mega hashmap
					System.out.println();
					set = sortByValue(megaHm).entrySet();
					itr = set.iterator();
					while (itr.hasNext()) {
						Map.Entry m = (Map.Entry) itr.next();
						System.out.println("Mega Number: " + m.getKey()
								+ " hit " + m.getValue());
						bw.append("Mega Number: " + m.getKey() + " hit "
								+ m.getValue() + lineSeparator);
						
						mhs.setMegaNum(Integer.parseInt((String) m.getKey()));
						mhs.setMegaHit((Integer)m.getValue());
						c.getMegaHits().add(mhs);
					}
					
					// transform Count to XML
					countToXML(c);
				
					// Create the initial vector
					ICombinatoricsVector<Integer> initialVector = Factory
							.createVector(vector);

					// Create the generator
					Generator<Integer> gen = Factory
							.createSimpleCombinationGenerator(initialVector, 5);

					bw.append("Vector: " + vector.toString() + lineSeparator);

					List<List> sortList = new ArrayList<List>();

					for (ICombinatoricsVector<Integer> perm : gen) {
						List<Integer> sort = new ArrayList<Integer>();
						sort.addAll(perm.getVector());
						Collections.sort(sort);
						sortList.add(sort);
						// bw.append("Combination without sort: "+perm.getVector()+lineSeparator);
						bw.append("Sort: " + sort.toString() + lineSeparator);
						// System.out.println("Combination without sort: \n"+
						// perm.getVector());
						// System.out.println("Sort: " + sort.toString());
					}

					bw.close();
					fw.close();

					Random rand = new Random();
					for (int x = 1; x <= 5; x++) {
						int randNum = rand.nextInt(sortList.size());
						System.out.println("lucky pick: "
								+ sortList.get(randNum).toString());
					}

					// Read XML(file) and write to String for sending to IIB
					readFileToString = readFile(FILE_NAME);
					// System.out.println(readFileToString);

					// Send to IIB
					// sendToIIB(readFileToString);

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// XML to Java Object
		/*
		 * MegaBalls mb2 = jaxbXMLToObject(); for (Iterator<MegaBall> iter =
		 * mb2.getMegaBall().listIterator(); iter .hasNext();) { MegaBall ball =
		 * iter.next(); System.out.println("Date: " +
		 * ball.getDate().toString()); System.out.println("Balls: " +
		 * ball.getNumbers().getNumber().toString());
		 * System.out.println("Mega Ball: " + ball.getMega().toString());
		 * System.out.println(); }
		 */

	}

	private static void sendToIIB(String message) throws JMSException,
			NamingException {

		// Start JMS Process~~~~~~~~~~~~~~~~~~

		// Hashtable env = new Hashtable();
		// env.put(Context.INITIAL_CONTEXT_FACTORY,
		// "com.ibm.websphere.naming.WsnInitialContextFactory");
		// env.put(Context.PROVIDER_URL,
		// "corbaloc:iiop:192.168.1.149:2809");

		InitialContext ctx = new InitialContext();
		// //
		System.out.println("**********************");
		// lookup the queue object
		Queue queue = (Queue) ctx.lookup("jms/megaqueue");

		// lookup the queue connection factory
		QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx
				.lookup("jms/megaqcf");

		// create a queue connection
		QueueConnection queueConn = connFactory.createQueueConnection();

		// create a queue session
		QueueSession queueSession = queueConn.createQueueSession(false,
				Session.DUPS_OK_ACKNOWLEDGE);

		// create a queue sender
		QueueSender queueSender = queueSession.createSender(queue);
		queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		// create a simple message by using readFileToString
		TextMessage message2 = queueSession.createTextMessage(message);

		// send the message
		queueSender.send(message2);

		// print what we did
		System.out.println("Sent msg to IIB: \n" + message2.getText());

		// close the queue connection
		queueConn.close();
	}

	private static void InsertToMegamillions(String date, Numbers numbers,
			String Mega) throws ParseException {
		String jdbcClassName = "com.ibm.db2.jcc.DB2Driver";
		String url = "jdbc:db2://192.168.1.73:50000/TESTDB2";
		String user = "db2admin";
		String password = "db2admin";
		@SuppressWarnings("deprecation")
		java.util.Date convertToDate = new java.util.Date(date);

		Connection con = null;
		try {
			// Load class into memory
			Class.forName(jdbcClassName);
			// Establish connection
			con = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				System.out.println("Connected successfully.");

				try {

					// query the the record from MegaMillions table
					PreparedStatement checkRecord = con
							.prepareStatement("SELECT date FROM DEVON.MEGAMILLIONS As T WHERE T.DATE = ?");
					checkRecord.setDate(1,
							new java.sql.Date(convertToDate.getTime()));

					// check if record exists
					if (!checkRecord.executeQuery().next()) {
						// prepare for insert
						PreparedStatement insert = con
								.prepareStatement("INSERT INTO DEVON.MEGAMILLIONS(DATE,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,MEGANUMBER)VALUES(?,?,?,?,?,?,?)");
						insert.setDate(1,
								new java.sql.Date(convertToDate.getTime()));
						insert.setString(2, numbers.getNumber().get(0));
						insert.setString(3, numbers.getNumber().get(1));
						insert.setString(4, numbers.getNumber().get(2));
						insert.setString(5, numbers.getNumber().get(3));
						insert.setString(6, numbers.getNumber().get(4));
						insert.setString(7, Mega);

						insert.executeUpdate();
						System.out.println(date
								+ " has been inserted into MegaMillions table");
					} else {
						System.out.println(date
								+ " exists in MegaMillions table");
					}

					con.close(); // close connection!!!
					System.out.println("Connection Closed.");
					System.out.println();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// important method for sorting the hashmap by value!
	private static Map sortByValue(Map map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	private static MegaBalls jaxbXMLToObject() {
		try {
			JAXBContext context = JAXBContext.newInstance(MegaBalls.class);
			Unmarshaller un = context.createUnmarshaller();
			MegaBalls mb = (MegaBalls) un.unmarshal(new File(FILE_NAME));
			return mb;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Count countxmltToObject() {
		try {
			JAXBContext context = JAXBContext.newInstance(Count.class);
			Unmarshaller un = context.createUnmarshaller();
			Count c = (Count) un.unmarshal(new File(FILE_NAME2));
			return c;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void megaBallsToXML(MegaBalls mb) throws IOException,
			XMLStreamException, FactoryConfigurationError {

		try {
			JAXBContext context = JAXBContext.newInstance(MegaBalls.class);

			Marshaller m = context.createMarshaller();
			// for pretty-print XML in JAXB
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FRAGMENT, true);

			// Write to System.out for debugging
			// m.marshal(mb, System.out);

			// Write to File
			// FileWriter fileWriter = new FileWriter(FILE_NAME, true);
			FileWriter fileWriter = new FileWriter(FILE_NAME);

			// m.marshal(mb, new File(FILE_NAME));
			m.marshal(mb, fileWriter);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private static void countToXML(Count count) throws IOException,
			XMLStreamException, FactoryConfigurationError {

		try {
			JAXBContext context = JAXBContext.newInstance(Count.class);

			Marshaller m = context.createMarshaller();
			// for pretty-print XML in JAXB
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FRAGMENT, true);

			// Write to System.out for debugging
			 //m.marshal(count, System.out);

			// Write to File
			// FileWriter fileWriter = new FileWriter(FILE_NAME, true);
			FileWriter fileWriter = new FileWriter(FILE_NAME2);

			// m.marshal(count, new File(FILE_NAME));
			m.marshal(count, fileWriter);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static String readFile(String filename) throws IOException {
		File file = new File(filename);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + lineSeparator);
				// fileContents.append(scanner.nextLine());
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}
}
