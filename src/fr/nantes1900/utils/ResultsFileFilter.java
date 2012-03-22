package fr.nantes1900.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import fr.nantes1900.constants.TextsKeys;

/**
 * Filter for results files.
 * 
 * @author Camille Bouquet
 */
public class ResultsFileFilter extends FileFilter {

	/**
	 * Results files extension.
	 */
	private String extension;

	/**
	 * Results files description.
	 */
	private String description;

	/**
	 * Type of the writer associated with this file filter
	 */
	private int writerType;
	
	/**
	 * Creates a new file filter associated with the writer type STL or CityGML.
	 * @param writerType
	 * 			Type of the writer associated with this file filter. If unknown, sets to stl writer.
	 */
	public ResultsFileFilter(int writerType) {
		switch (writerType) {
		case AbstractWriter.STL_WRITER:
			this.extension = "stl";
			this.description = FileTools
					.readElementText(TextsKeys.KEY_FILESTLDESCRIPTION);
			break;
		case AbstractWriter.CITYGML_WRITER:
			this.extension = "citygml";
			this.description = FileTools
					.readElementText(TextsKeys.KEY_FILECITYGMLDESCRIPTION);
			break;
		default:
			System.err.println("Writer type unknown.");
			this.extension = "stl";
			this.description = FileTools
					.readElementText(TextsKeys.KEY_FILESTLDESCRIPTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public final boolean accept(final File file) {
		return (file.isDirectory() || file.getName().endsWith(this.extension));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public final String getDescription() {
		return "." + this.extension + " - " + this.description;
	}

	/**
	 * Gets the extension of the filer.
	 * 
	 * @return the extension
	 */
	public final String getExtension() {
		return this.extension;
	}
	
	public int getWriterType() {
		return this.writerType;
	}
}