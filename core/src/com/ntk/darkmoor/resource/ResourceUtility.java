package com.ntk.darkmoor.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ntk.darkmoor.exception.LoadException;

public class ResourceUtility {
	
	public static Element extractRootElement(String fileName) {
		XmlReader reader = new XmlReader();
		InputStream inputStream = null;
		Element root = null;

		if (ResourceUtility.isStandaloneMode() && new File(fileName).exists()) {
			try {
				inputStream = new FileInputStream(fileName);
				root = reader.parse(inputStream);
			} catch (FileNotFoundException e) {
				throw new LoadException("[GraphicAssets]: file '" + fileName + "' could not be opened");
			} catch (IOException e) {
				throw new LoadException("[GraphicAssets]: external file '" + fileName + "' could not be parsed");
			}
		} else {
			FileHandle fh = Gdx.files.internal(fileName);
			try {
				root = reader.parse(fh);
			} catch (IOException e) {
				throw new LoadException("[GraphicAssets]: internal file '" + fileName + "' could not be parsed");
			}
		}
		return root;
	}

	public static boolean isStandaloneMode() {
		return (Gdx.files == null);
	}
}
