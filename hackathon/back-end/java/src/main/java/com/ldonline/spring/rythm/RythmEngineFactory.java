package com.ldonline.spring.rythm;

import org.rythmengine.RythmEngine;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class RythmEngineFactory {
	private static RythmEngine eng = null;
	
	public static RythmEngine getEngine(HttpServletRequest req, String rootDirectory) {
		if (eng == null) {
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("engine.mode", "dev");
			props.put("home.template.dir", rootDirectory);
			props.put("engine.file_write", false);
			eng = new RythmEngine(props);
		}
		return eng;
	}
}
