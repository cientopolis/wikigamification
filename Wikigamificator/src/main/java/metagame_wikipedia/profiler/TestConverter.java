package metagame_wikipedia.profiler;

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

public class TestConverter extends TextConverter implements CompleteWikitextVisitorNoReturn{

	public TestConverter(WikiConfig config, int wrapCol) {
		super(config, wrapCol);
	}

	
	public void visit(WtNode n)
	{
		// Fallback for all nodes that are not explicitly handled below
		System.out.println("<");
		System.out.println(n.getNodeName());
		System.out.println(" />");
	}
	
	public void visit(WtNodeList n)
	{
		System.out.println("List");
		System.out.println(n.getRtd().toString());
		iterate(n);
	}

	public void visit(WtUnorderedList e)
	{
		System.out.println("UnorderedList");
//		System.out.println(e.getRtd().toString());
		iterate(e);
	}

	public void visit(WtOrderedList e)
	{
		System.out.println("OrderedList");
		System.out.println(e.getRtd().toString());
		iterate(e);
	}

	public void visit(WtListItem item)
	{
		System.out.println("List Item");
		System.out.println(item.getRtd().toString());
		iterate(item);
	}

	public void visit(EngPage p)
	{
		System.out.println("Eng Page");
		iterate(p);
	}

	public void visit(WtText text)
	{
		System.out.println("Text");
		System.out.println(text.getContent());
		iterate(text);
	}

	public void visit(WtWhitespace w)
	{
		
		System.out.println("Whitespace");
		System.out.println(w.getRtd().toString());
		iterate(w);
		//write(" ");
	}

	public void visit(WtBold b)
	{
		System.out.println("**Bold**");
		iterate(b);
		System.out.println(b.getRtd().toString());
		System.out.println("**Bold**");
	}

	public void visit(WtItalics i)
	{
		System.out.println("//Italics//");
		iterate(i);
		System.out.println(i.getRtd().toString());
		System.out.println("//Italics//");
	}

	public void visit(WtXmlCharRef cr)
	{
		System.out.println("WtXmlCharRef");
		System.out.println(cr.getRtd().toString());
		System.out.println(Character.toChars(cr.getCodePoint()));
		iterate(cr);
		
	}

	public void visit(WtXmlEntityRef er)
	{
		System.out.println("WtXmlEntityRef");
		System.out.println(er.getRtd().toString());
		System.out.println(er.getName());
		
		String ch = er.getResolved();
		if (ch == null)
		{
			System.out.println('&');
			System.out.println(er.getName());
			System.out.println(';');
		}
		else
		{
			System.out.println(ch);
		}
		iterate(er);
	}

	public void visit(WtUrl wtUrl)
	{
		System.out.println("URL");
		System.out.println(wtUrl.getRtd().toString());
		System.out.println(wtUrl.getPath());
		iterate(wtUrl);
	}

	public void visit(WtExternalLink link)
	{
		System.out.println("[[External Link]]");
		extLinkNum++;
		System.out.println(link.getRtd().toString());
		System.out.println(link.getTitle());
		System.out.println(link.getTarget().getPath());
		iterate(link);
	}

	public void visit(WtInternalLink link)
	{
		System.out.println("[Internal Link]");
		System.out.println(link.getRtd().toString());
		System.out.println(link.getTitle());
		System.out.println(link.getTarget().getAsString());
		iterate(link);
		}

	public void visit(WtSection s)
	{
		System.out.println("Section");
//		System.out.println(s.getRtd().toString());
		System.out.println(s.getLevel());
		iterate(s);
	}

	public void visit(WtParagraph p)
	{
		System.out.println("Paragraph");
//		System.out.println(p.getRtd().toString());
		iterate(p);
		
	}

	public void visit(WtHorizontalRule hr)
	{
		System.out.println("WtHorizontalRule");
		System.out.println(hr.getRtd().toString());
		iterate(hr);
//		newline(1);
//		write(StringTools.strrep('-', wrapCol));
//		newline(2);
	}

	public void visit(WtXmlElement e)
	{
		System.out.println("XmlElement");
		System.out.println(e.getRtd().toString());
		System.out.println(e.getName().equalsIgnoreCase("br"));
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
		System.out.println("Image");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getAlt());
		iterate(n);
	}

	public void visit(WtIllegalCodePoint n)
	{
		System.out.println("WtIllegalCodePoint");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getCodePoint().toString());
		iterate(n);
	}

	public void visit(WtXmlComment n)
	{
		System.out.println("WtXmlComment");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getContent());
		iterate(n);
	}

	public void visit(WtTemplate n)
	{
		System.out.println("WtTemplate");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getName().getAsString());
		iterate(n);
	}

	public void visit(WtTemplateArgument n)
	{
		System.out.println("WtTemplateArgument");
		System.out.println(n.getRtd().toString());
		if (n.getName().isResolved()) {
			System.out.println(n.getName().getAsString());
		}
		iterate(n);
	}

	public void visit(WtTemplateParameter n)
	{
		System.out.println("WtTemplateParameter");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getName().getAsString());
		iterate(n);
	}

	public void visit(WtTagExtension n)
	{
		System.out.println("WtTagExtension");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getName());
		iterate(n);
	}

	public void visit(WtPageSwitch n)
	{
		System.out.println("WtPageSwitch");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getName());
		iterate(n);
	}


	public void visit(WtLinkOptionLinkTarget n) {
		System.out.println("WtLinkOptionLinkTarget");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getTarget().getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtRedirect n) {
		System.out.println("WtRedirect");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getTarget().getAsString());
		iterate(n);
	}


	public void visit(WtTableImplicitTableBody n) {
		System.out.println("WtTableImplicitTableBody");
//		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtXmlAttribute n) {
		System.out.println("WtXmlAttribute");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getName());
//		System.out.println(n.getValue().getRtd().toString() );
		iterate(n);
		
	}


	public void visit(WtXmlEmptyTag n) {
		System.out.println("WtXmlEmptyTag");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getName() );
		iterate(n);
		
	}


	public void visit(WtXmlStartTag n) {
		System.out.println("WtXmlStartTag");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getName() );
		iterate(n);
		
	}


	public void visit(WtImStartTag n) {
		System.out.println("WtImStartTag");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getName() );
		iterate(n);
		
	}


	public void visit(WtTable n) {
		System.out.println("WtTable");
		System.out.println(n.getRtd().toString());
//		System.out.println(n.getBody().getRtd().toString() );
		iterate(n);
		
	}


	public void visit(WtTableCaption n) {
		System.out.println("WtTableCaption");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getBody().getRtd().toString() );
		iterate(n);
		
	}


	public void visit(WtTableCell n) {
		System.out.println("WtTableCell");
		System.out.println(n.getRtd().toString());
//		System.out.println(n.getBody().getRtd().toString() );
		iterate(n);
		
	}


	public void visit(WtTableHeader n) {
		System.out.println("WtTableHeader");
		System.out.println(n.getRtd().toString());
//		System.out.println(n.getBody().getRtd().toString() );
		iterate(n);
		
	}


	public void visit(WtTableRow n) {
		System.out.println("WtTableRow");
		System.out.println(n.getRtd().toString());
//		System.out.println(n.getBody().getRtd().toString() );
		iterate(n);
		
	}


	public void visit(WtLinkOptionKeyword n) {
		System.out.println("WtLinkOptionKeyword");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getKeyword() );
		iterate(n);
		
	}


	public void visit(WtLinkOptionResize n) {
		System.out.println("WtLinkOptionKeyword");
		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtSignature n) {
		System.out.println("WtLinkOptionKeyword");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getTildeCount() );
		iterate(n);
		
	}


	public void visit(WtTicks n) {
		System.out.println("WtTicks");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getTickCount() );
		iterate(n);
		
	}


	public void visit(WtXmlEndTag n) {
		System.out.println("WtXmlEndTag");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getName());
		iterate(n);
		
	}


	public void visit(WtImEndTag n) {
		System.out.println("WtImEndTag");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getName());
		iterate(n);
		
	}


	public void visit(WtBody n) {
		System.out.println("WtBody");
//		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtDefinitionList n) {
		System.out.println("WtDefinitionList");
//		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtDefinitionListDef n) {
		System.out.println("WtDefinitionListDef");
		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtDefinitionListTerm n) {
		System.out.println("WtDefinitionListTerm");
		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtHeading n) {
		System.out.println("WtHeading");
		System.out.println(n.getRtd().toString());
		System.out.println(n.toString());
		iterate(n);
		
	}


	public void visit(WtLinkOptionAltText n) {
		System.out.println("WtLinkOptionAltText");
		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtLinkOptions n) {
		System.out.println("WtLinkOptions");
//		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtLinkTitle n) {
		System.out.println("WtLinkTitle");
//		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtName n) {
		System.out.println("WtName");
//		System.out.println(n.getRtd().toString());
		if (n.isResolved()) {
			System.out.println(n.getAsString());
		}
		iterate(n);
		
	}


	public void visit(WtOnlyInclude n) {
		System.out.println("WtOnlyInclude");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getElementType().name());
		iterate(n);
	}


	public void visit(WtParsedWikitextPage n) {
		System.out.println("WtParsedWikitextPage");
		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtPreproWikitextPage n) {
		System.out.println("WtPreproWikitextPage");
		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtSemiPre n) {
		System.out.println("WtSemiPre");
		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtSemiPreLine n) {
		System.out.println("WtSemiPreLine");
		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtTemplateArguments n) {
		System.out.println("WtTemplateArguments");
//		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtValue n) {
		System.out.println("WtValue");
//		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtXmlAttributes n) {
		System.out.println("WtXmlAttributes");
//		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}


	public void visit(WtIgnored n) {
		System.out.println("WtIgnored");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getContent());
		iterate(n);
		
	}


	public void visit(WtLinkOptionGarbage n) {
		System.out.println("WtLinkOptionGarbage");
		System.out.println(n.getRtd().toString());
		iterate(n);
		
		
	}


	public void visit(WtNewline n) {
		System.out.println("WtNewline");
		System.out.println(n.getRtd().toString());
		System.out.println(n.getContent());
		iterate(n);
		
		
	}


	public void visit(WtPageName n) {
		System.out.println("WtPageName");
//		System.out.println(n.getRtd().toString());
		System.out.println(n.getAsString());
		iterate(n);		
	}


	public void visit(WtTagExtensionBody n) {
		System.out.println("WtTagExtensionBody");
//		System.out.println(n.getRtd().toString());
		System.out.println(n.getContent());
		iterate(n);		
	}


	public void visit(WtXmlAttributeGarbage n) {
		System.out.println("WtXmlAttributeGarbage");
//		System.out.println(n.getRtd().toString());
		iterate(n);
		
	}
	
}
