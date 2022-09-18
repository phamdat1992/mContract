package com.ldonline.spring.rythm;

import org.rythmengine.RythmEngine;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class SimpleRythmViewResolver implements ViewResolver {
	private String rootDirectory;
	private String suffix;
	
	private RythmEngine rythmEngine = null;
	
	@Override
	public View resolveViewName(String name, Locale loc) throws Exception {
		return new SimpleRythmView(this, name);
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public RythmEngine getRythmEngine() {
		return rythmEngine;
	}
}
