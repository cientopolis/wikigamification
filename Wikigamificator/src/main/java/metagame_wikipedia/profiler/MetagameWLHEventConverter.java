package metagame_wikipedia.profiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngPage;
import org.sweble.wikitext.parser.nodes.CompleteWikitextVisitorNoReturn;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtBold;
import org.sweble.wikitext.parser.nodes.WtDefinitionList;
import org.sweble.wikitext.parser.nodes.WtDefinitionListDef;
import org.sweble.wikitext.parser.nodes.WtDefinitionListTerm;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtHeading;
import org.sweble.wikitext.parser.nodes.WtHorizontalRule;
import org.sweble.wikitext.parser.nodes.WtIgnored;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint;
import org.sweble.wikitext.parser.nodes.WtImEndTag;
import org.sweble.wikitext.parser.nodes.WtImStartTag;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtItalics;
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText;
import org.sweble.wikitext.parser.nodes.WtLinkOptionGarbage;
import org.sweble.wikitext.parser.nodes.WtLinkOptionKeyword;
import org.sweble.wikitext.parser.nodes.WtLinkOptionLinkTarget;
import org.sweble.wikitext.parser.nodes.WtLinkOptionResize;
import org.sweble.wikitext.parser.nodes.WtLinkOptions;
import org.sweble.wikitext.parser.nodes.WtLinkTitle;
import org.sweble.wikitext.parser.nodes.WtListItem;
import org.sweble.wikitext.parser.nodes.WtName;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtOnlyInclude;
import org.sweble.wikitext.parser.nodes.WtOrderedList;
import org.sweble.wikitext.parser.nodes.WtPageName;
import org.sweble.wikitext.parser.nodes.WtPageSwitch;
import org.sweble.wikitext.parser.nodes.WtParagraph;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;
import org.sweble.wikitext.parser.nodes.WtRedirect;
import org.sweble.wikitext.parser.nodes.WtSection;
import org.sweble.wikitext.parser.nodes.WtSemiPre;
import org.sweble.wikitext.parser.nodes.WtSemiPreLine;
import org.sweble.wikitext.parser.nodes.WtSignature;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtTableCaption;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtTableImplicitTableBody;
import org.sweble.wikitext.parser.nodes.WtTableRow;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtTemplateArguments;
import org.sweble.wikitext.parser.nodes.WtTemplateParameter;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtTicks;
import org.sweble.wikitext.parser.nodes.WtUnorderedList;
import org.sweble.wikitext.parser.nodes.WtUrl;
import org.sweble.wikitext.parser.nodes.WtValue;
import org.sweble.wikitext.parser.nodes.WtWhitespace;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlAttributeGarbage;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;
import org.sweble.wikitext.parser.nodes.WtXmlCharRef;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.nodes.WtXmlEmptyTag;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlEntityRef;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import de.fau.cs.osr.utils.StringTools;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.content.DiffAction;

public class MetagameWLHEventConverter extends MetagameEventConverter implements CompleteWikitextVisitorNoReturn{

	private static List<String> source = new LinkedList<String>();
	private static List<String> dest = new LinkedList<String>();
	

	public MetagameWLHEventConverter(WikiConfig config, int wrapCol, List<MetagameEvent> events, DiffAction action,
			Date timestamp, Integer playerId, String playerEmail, Integer articleNs, String page,Map<String, String> proyects) {
		
		super(config, wrapCol, events, action, timestamp, playerId, playerEmail, articleNs,source.get(dest.indexOf(page)),proyects);
		this.page=page;
	}

	
	public static List<String> getSource() {
		return source;
	}


	public static void setSource(List<String> source) {
		MetagameWLHEventConverter.source = source;
	}


	public static List<String> getDest() {
		return dest;
	}


	public static void setDest(List<String> dest) {
		MetagameWLHEventConverter.dest = dest;
	}


	public List<MetagameEvent> getEvents() {
		return events;
	}


	public void setEvents(List<MetagameEvent> events) {
		this.events = events;
	}


	public DiffAction getAction() {
		return action;
	}


	public void setAction(DiffAction action) {
		this.action = action;
	}


	public Date getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}


	public Integer getPlayerId() {
		return playerId;
	}


	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}


	public String getPlayerEmail() {
		return playerEmail;
	}


	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}
	public String getEventType(String eventname){
		if (action.compareTo(DiffAction.INSERT)==0||action.compareTo(DiffAction.FULL_REVISION_UNCOMPRESSED)==0) {
			List<String> contribution= Arrays.asList(new String[]{

				});
					
			List<String> reinforcement= Arrays.asList(new String[]{
					
				});
			List<String> disemination= Arrays.asList(new String[]{
					"WhatsLinksHere"
				});
			if (contribution.contains(eventname)) {
				return "contribution";
			}else if (reinforcement.contains(eventname)) {
				return "reinforcement";
			}else if (disemination.contains(eventname)) {
				return "dissemination";
			}	
		}
//		else if (action.compareTo(DiffAction.CUT)==0 || 
//				action.compareTo(DiffAction.DELETE)==0 ||
//				action.compareTo(DiffAction.PASTE)==0 ||
//				action.compareTo(DiffAction.REPLACE)==0) {
//			return "Reinforcement";
//		}
		
		
		
		return "Null";
	}

	static ArrayList<Integer> indexOfAll(String obj, List<String> dest2){
	    ArrayList<Integer> indexList = new ArrayList<Integer>();
	    for (int i = 0; i < dest2.size(); i++)
	        if(obj.equals(dest2.get(i)))
	            indexList.add(i);
	    return indexList;
	}
	
	public void visit(WtNode n)
	{
//		 Fallback for all nodes that are not explicitly handled below
//		System.out.println("<");
//		System.out.println(n.getNodeName());
//		System.out.println(" />");
	}
	
	public void visit(WtNodeList n)
	{
		//System.out.println("List");
		iterate(n);
	}

	public void visit(WtUnorderedList e)
	{
		//System.out.println("UnorderedList");
		iterate(e);
	}

	public void visit(WtOrderedList e)
	{
		//System.out.println("OrderedList");

		iterate(e);
	}

	public void visit(WtListItem item)
	{
		//System.out.println("List Item");
		iterate(item);
	}

	public void visit(EngPage p)
	{
		//System.out.println("Eng Page");
		iterate(p);
	}

	public void visit(WtText text)
	{
		//System.out.println("Text");
//		events.add(new MetagameEvent(playerEmail, 
//				this.getEventType("Text"), action, 
//				"Text", timestamp));
	}

	public void visit(WtWhitespace w)
	{
		//write(" ");
	}

	public void visit(WtBold b)
	{
		//System.out.println("**Bold**");
		iterate(b);
		//System.out.println("**Bold**");

	}

	public void visit(WtItalics i)
	{
		//System.out.println("//Italics//");
		iterate(i);
		//System.out.println("//Italics//");

	}

	public void visit(WtXmlCharRef cr)
	{
		//write(Character.toChars(cr.getCodePoint())); no se que es
		int value = cr.getCodePoint();
		iterate(cr);

		
	}

	public void visit(WtXmlEntityRef er)//esto es el numero de la referencia en la palabra, puede ser vacio
	{
		iterate(er);

		String ch = er.getResolved();
//		if (ch == null)
//		{
//			write('&');
//			write(er.getName());
//			write(';');
//		}
//		else
//		{
//			write(ch);
//		}
	}

	public void visit(WtUrl wtUrl)
	{
//		System.out.println("URL");
		iterate(wtUrl);
	}

	public void visit(WtExternalLink link)
	{
		//System.out.println("[[External Link]]");
		extLinkNum++;
		iterate(link);
	}

	public void visit(WtInternalLink link)
	{
	//	System.out.println("[Internal Link]");
		iterate(link);
		String target = link.getTarget().getAsString();
		
		for (Integer index : indexOfAll(page, dest)) {
			if (source.get(index).equalsIgnoreCase(target)) {
				events.add(new MetagameEvent(playerEmail, 
						this.getEventType("WhatsLinksHere"), action, 
						"WhatsLinksHereInternalLink", timestamp,page,proyect));
			}
		}
		}

	public void visit(WtSection s)
	{
//		System.out.println("Section");
		iterate(s);
	}

	public void visit(WtParagraph p)
	{
		//System.out.println("Paragraph");
		iterate(p);

		
	}

	public void visit(WtHorizontalRule hr)
	{
//		newline(1);
//		write(StringTools.strrep('-', wrapCol));
//		newline(2);
		iterate(hr);
	}

	public void visit(WtXmlElement e)
	{
//		System.out.println("XmlElement");
		if (e.getName().equalsIgnoreCase("br"))
		{
			//newline(1);
		}
		else
		{
			iterate(e.getBody());
		}
	}

	// =========================================================================
	// Stuff we want to hide

	public void visit(WtImageLink n)
	{
//		System.out.println("Image");
		iterate(n);
	}

	public void visit(WtIllegalCodePoint n)
	{
		iterate(n);
	
	}

	public void visit(WtXmlComment n)
	{
		iterate(n);
	}

	public void visit(WtTemplate n)
	{
		iterate(n);

	}

	public void visit(WtTemplateArgument n)
	{
		iterate(n);

	}

	public void visit(WtTemplateParameter n)
	{
		iterate(n);

	}

	public void visit(WtTagExtension n)
	{
		iterate(n);
	}

	public void visit(WtPageSwitch n)
	{
		iterate(n);
	}

	public void visit(WtLinkOptionLinkTarget n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtRedirect n) {
		iterate(n);
		String target = n.getTarget().getAsString();
		
		for (Integer index : indexOfAll(page, dest)) {
			if (source.get(index).equalsIgnoreCase(target)) {
				events.add(new MetagameEvent(playerEmail, 
						this.getEventType("WhatsLinksHere"), action, 
						"WhatsLinksHereRedirect", timestamp,page,proyect));
			}
		}
		
	}

	public void visit(WtTableImplicitTableBody n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtXmlAttribute n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtXmlEmptyTag n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtXmlStartTag n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtImStartTag n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtTable n) {
		// TODO Auto-generated method stub
		iterate(n);

	}

	public void visit(WtTableCaption n) {
		iterate(n);

	}

	public void visit(WtTableCell n) {
		iterate(n);

		
	}

	public void visit(WtTableHeader n) {
		iterate(n);

	}

	public void visit(WtTableRow n) {
		iterate(n);

		
	}

	public void visit(WtLinkOptionKeyword n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtLinkOptionResize n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtSignature n) {
		iterate(n);
		
	}

	public void visit(WtTicks n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtXmlEndTag n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtImEndTag n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtBody n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtDefinitionList n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtDefinitionListDef n) {
//		System.out.println("Detector de  :");
		if (n.getRtd().toString().contains(":")) {

//			System.out.println("number of : = "+StringUtils.countOccurrencesOf(n.getRtd().toString(), ":"));

		}
		
	}

	public void visit(WtDefinitionListTerm n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtHeading n) {
		// TODO Auto-generated method stub
		iterate(n);

	}

	public void visit(WtLinkOptionAltText n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtLinkOptions n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtLinkTitle n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtName n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtOnlyInclude n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtParsedWikitextPage n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtPreproWikitextPage n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtSemiPre n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtSemiPreLine n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtTemplateArguments n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtValue n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtXmlAttributes n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtIgnored n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtLinkOptionGarbage n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtNewline n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtPageName n) {
		
	}

	public void visit(WtTagExtensionBody n) {
		// TODO Auto-generated method stub
		iterate(n);
	}

	public void visit(WtXmlAttributeGarbage n) {
		// TODO Auto-generated method stub
		iterate(n);
	}
}
