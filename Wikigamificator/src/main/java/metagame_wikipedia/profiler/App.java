package metagame_wikipedia.profiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.api.Revision;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.api.RevisionAPIConfiguration;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.api.RevisionApi;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.content.DiffAction;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.content.DiffPart;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {
		String rootpath = args[0];
        String sep = ",";
        
		//Structura de salvado
		List<MetagameEvent> events = new LinkedList<MetagameEvent>();
		
		// CONFIGURATIONS
		//Revision API
		RevisionAPIConfiguration config = new RevisionAPIConfiguration();
		config.setHost("localhost");
		config.setDatabase("wikigame");
		config.setUser("wikigame");
		config.setPassword("wikigame");
		config.setCharacterSet("UTF-8");
		config.setBufferSize(20000);
		config.setMaxAllowedPacket(1024 * 1024);
		RevisionApi revapi = new RevisionApi(config);
		Revision r;
		
		// Set-up a simple wiki configuration
		WikiConfig configsweble = DefaultConfigEnWp.generate();
		final int wrapCol = 80;
		// Instantiate a compiler for wiki pages
		WtEngineImpl engine = new WtEngineImpl(configsweble);
		
		//Test
		//test(revapi, configsweble, wrapCol, engine);

		Map<String, String> proyects = relationsCategories(rootpath);
		
		//Main Article process
		String file = "/articles.csv";
		Class converter = MetagameEventConverter.class;
		eventCreator(rootpath, sep, events, revapi, configsweble, wrapCol, engine, file, converter,proyects);
		
		//Talk Article process
		file = "/talk/articles.csv";
		converter = MetagameTalkEventConverter.class;
		eventCreator(rootpath, sep, events, revapi, configsweble, wrapCol, engine, file, converter,proyects);
		
		//Whats Link Here process
		String fileArticles = "/whatlinkshere/articles.csv";
		String fileRelations = "/whatlinkshere/relations.csv";
		whatsLinkHere(rootpath, sep, events, revapi, configsweble, wrapCol, engine, fileArticles, fileRelations,proyects);
		
		
		Collections.sort(events);
		printCSVEvents(rootpath, events);
		//sendEvents(events);
		
		
		//trash(revapi);

	}
	private static Map<String, String> relationsCategories(String rootpath)
			throws FileNotFoundException, LinkTargetException, WikiApiException {
		BufferedReader br;
		br = new BufferedReader(new FileReader(rootpath+"/relations.csv"));
		Scanner sc = new Scanner(br); 
		Map<String, String> relations = new HashMap<String, String>();
//		int test = 0;//for test
		while (sc.hasNextLine()) {
			String string = sc.nextLine();
			if (string.trim().isEmpty()) {
				continue;
			}
			//Name,id,ns
			String[] articles = string.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			String category = articles[0].replace("\"", "");
			//category= "cientopolis";
			String article = articles[1].replace("\"", "");
			relations.put(article, category);
		}
		sc.close();
		
		return relations;
	}
	private static void whatsLinkHere(String rootpath, String sep, List<MetagameEvent> events, RevisionApi revapi,
			WikiConfig configsweble, final int wrapCol, WtEngineImpl engine, String fileArticles,String fileRelations, Map<String, String> proyects)
			throws FileNotFoundException, LinkTargetException, WikiApiException {
		BufferedReader br;
		br = new BufferedReader(new FileReader(rootpath+fileRelations));
		Scanner sc = new Scanner(br); 
		List<String> source = new LinkedList<String>();
		List<String> dest = new LinkedList<String>();
//		int test = 0;//for test
		while (sc.hasNextLine()) {
			String string = sc.nextLine();
			if (string.trim().isEmpty()) {
				continue;
			}
			//Name,id,ns
			String[] articles = string.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			source.add(articles[0].replace("\"", ""));
			dest.add(articles[1].replace("\"", ""));
		}
		sc.close();
		
		MetagameWLHEventConverter.setDest(dest);
		MetagameWLHEventConverter.setSource(source);
		Class converter = MetagameWLHEventConverter.class;
		eventCreator(rootpath, sep, events, revapi, configsweble, wrapCol, engine, fileArticles, converter,proyects);
		
	}
	
	
	
	private static void eventCreator(String rootpath, String sep, List<MetagameEvent> events, RevisionApi revapi,
			WikiConfig configsweble, final int wrapCol, WtEngineImpl engine, String file, Class converter, Map<String, String> proyects)
			throws FileNotFoundException, LinkTargetException, WikiApiException {
		BufferedReader br;
		Revision r;
		br = new BufferedReader(new FileReader(rootpath+file));
		Scanner sc = new Scanner(br); 

		while (sc.hasNextLine()) {
			
			String string = sc.nextLine();
			if (string.trim().isEmpty()) {
				continue;
			}
			//Name,id,ns
			String[] article = string.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			String articleTitle = article[0].replace("\"", "");
			if (article[1].replace("\"", "").equalsIgnoreCase("NA")) {
				continue;
			}
			Integer articleID = new Integer(article[1].replace("\"", ""));
			Integer articleNS = new Integer(article[2].replace("\"", ""));
			PageTitle pageTitle = PageTitle.make(configsweble, articleTitle);
			PageId pageId = new PageId(pageTitle, articleID);
			String proyect;
			if (proyects.containsKey(articleTitle)) {
				proyect = proyects.get(articleTitle);
			}else {
				proyect = proyects.get(articleTitle.replace("Talk:", ""));
			}
			
			//catch if the article didn't exist
			int numberOfRevisions =0;
			try {
				numberOfRevisions = revapi.getNumberOfRevisions(articleID);
			} catch (Exception e) {
				continue;
			}
			System.out.println(articleTitle);
			for (int i = 1; i <= numberOfRevisions; i++) {
				r = revapi.getRevision(articleID, i);
				if (i%1000==0) {
					System.out.println(i+"-Time-"+r.getTimeStamp());
					
				}
				//System.out.println(i);
//				System.out.println(r.getTimeStamp());
				//por cada revision hago un logeo
				String contributorName = r.getContributorName();
				if (!r.contributorIsRegistered()) {
					contributorName="Anonymous";
					//continue;
				}
				events.add(new MetagameEvent(contributorName, "login", DiffAction.INSERT, "login", r.getTimeStamp(), articleTitle,proyect));
				Collection<DiffPart> diffparts = r.getParts();
				for (Iterator<DiffPart> iterator = diffparts.iterator(); iterator.hasNext();) {
					DiffPart diffPart = (DiffPart) iterator.next();
//					System.out.println(diffPart.getAction().name());		
					if (diffPart.getText() != null) {
						try {
							EngProcessedPage cp = engine.postprocess(pageId, diffPart.getText(), null);
							MetagameEventConverter parser = 
									(MetagameEventConverter) converter.getDeclaredConstructors()[0].newInstance(configsweble, wrapCol,
							events,diffPart.getAction(),r.getTimeStamp(),r.getContributorId()
							,contributorName,articleNS,articleTitle,proyects);

							//System.out.println((String) 
							parser.go(cp.getPage());//);
						} catch (Exception e) {
							System.out.println("ERROR:" + e.getMessage());
							diffPart.getText();
						}
					}
				}
			}
		}
		
		
		sc.close();
	}


	private static void printCSVEvents(String path,List<MetagameEvent> events) {
		File archive = new File(path+"/events.csv");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(archive);
		
	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write("|Time|,|Action|,|Event Type|,|Event Name|,|Player|,|Page|,|Project|");
		bw.newLine();
		for (MetagameEvent metagameEvent : events) {
			bw.write("|"+metagameEvent.getTimestamp().toString()+"|");
			bw.write(",");
			bw.write("|"+metagameEvent.getAction().name()+"|");
			bw.write(",");
			bw.write("|"+metagameEvent.getEventType()+"|");
			bw.write(",");
			bw.write("|"+metagameEvent.getEventName()+"|");
			bw.write(",");
			bw.write("|"+metagameEvent.getPlayerEmail()+"|");
			bw.write(",");
			bw.write("|"+metagameEvent.getPage()+"|");
			bw.write(",");
			if (metagameEvent.getProyect()==null) {
				bw.write("|Null|");
				
			}else {
				
				bw.write("|"+metagameEvent.getProyect()+"|");
			}
			bw.newLine();
		}

	 
		bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
