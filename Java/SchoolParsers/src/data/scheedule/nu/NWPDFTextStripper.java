/**
 * NWPDFTextStripper.java
 * @copyright Scheedule, Inc.
 */
package data.scheedule.nu;

import java.awt.Rectangle;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.apache.pdfbox.util.TextPosition;

/**
 * Helper class that strips the text from the Northwestern course PDF for the Course Pages
 * A separate class was created so I could modify the outputted text to gain some additional information about the text.
 * For example, for each line, if the text is font size 12, I prefix >>> before the text so we can get some contextual information on how large the font is, since it's not represented at all with plaintext
 */
public class NWPDFTextStripper extends PDFTextStripperByArea {

	public static final String LeftColumn = "left column";
	public static final String RightColumn = "right column";
	
	private float _lastSize = 0.0f;
	private float _lastY = 0.0f;
	private PDPage _page;
	
	/**
	 * Creates a new instance of the Northwestern PDF Text Stripper
	 * @throws IOException
	 */
	public NWPDFTextStripper() throws IOException {
		super();
		setSortByPosition(true);
	}

	/**
	 * Extracts text from the 2 equal width columns on the given PDF Page
	 * @param page Page to extract text from 2 columns from
	 * @throws IOException
	 */
	public void extractColumns(PDPage page) throws IOException {
		// Fix the regions
		_page = page;
		getRegions().clear();
		
		// Add 2 regions, the left and right columns, they are just Rectangles in pixels on the page of which areas to look for text
		// Assume each column is equal width, and skip the bottom 25 pixels since they are where the page # and some * text is
		PDRectangle r = page.findCropBox();	// Don't know if this is the right choice, but it worked, find the dimensions of the page
		int columnWidth = (int)(r.getWidth() / 2);
		int columnHeight = (int)(r.getHeight() - 25);
		addRegion(LeftColumn, new Rectangle(0, 0, columnWidth, columnHeight));
		addRegion(RightColumn, new Rectangle(columnWidth, 0, columnWidth, columnHeight));
		
		// Call the extract regions parent function
		extractRegions(page);
	}
	
	/**
	 * @return The text found in the left column
	 */
	public String getLeftColumnText() {
		return (getRegions().size() > 0) ? getTextForRegion(LeftColumn) : null;
	}
	
	/**
	 * @return The text found in the right column
	 */
	public String getRightColumnText() {
		return (getRegions().size() > 0) ? getTextForRegion(RightColumn) : null;
	}
	
	/**
	 * @see PDFTextStripperByArea#processTextPosition()
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void processTextPosition(TextPosition text) 
	{
		float textSize = text.getFontSizeInPt();
		float textY = text.getYDirAdj();
		if(textSize == 4.0f) {
			return;
		}

		if(_lastSize != textSize || _lastY != textY) {
			int numberChars = 0;
			if(textSize == 12.0f) {
				numberChars = 3;
			}
			else if(textSize == 8.0f) {
				numberChars = 2;
			}
						
			// Add the # of > chars
			for(int i = 0; i < numberChars; i++) {
				TextPosition ch = new TextPosition(_page, text.getTextPos(), text.getTextPos(), text.getFontSize(), text.getIndividualWidths(), text.getWidthOfSpace(), ">", text.getFont(), text.getFontSize(), (int)text.getFontSizeInPt(), text.getWordSpacing());
				super.processTextPosition(ch);
			}
			
			_lastSize = textSize;
			_lastY = textY;
		}
		
		super.processTextPosition(text);
	}
}
