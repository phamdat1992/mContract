package com.ldonline.spring.rythm;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.rythmengine.RythmEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

public class SimpleRythmView implements View {
	private static Logger LOG = LoggerFactory.getLogger(SimpleRythmView.class);

	private SimpleRythmViewResolver ctx;
	private String name;

	public SimpleRythmView(SimpleRythmViewResolver simpleRythmViewResolver, String name) {
		this.ctx = simpleRythmViewResolver;
		this.name = name;
	}

	@Override
	public String getContentType() {
		return "text/html";
	}

//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Override
//	public void render(Map model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		resp.setContentType("text/html");
//		RythmEngine eng = RythmEngineFactory.getEngine(req, ctx.getRootDirectory());
//
//		model.put("AppRoot", req.getSession().getServletContext().getContextPath());
//
//		String filePath = ctx.getRootDirectory() + name + ctx.getSuffix();
//		File file = new File(filePath);
//		if (!file.exists()) {
//			FileUtils.copyURLToFile(this.getClass().getClassLoader().getResource(filePath), file);
//		}
//		String s = eng.render(file, model);
//		resp.getWriter().write(s);
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void render(Map model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		resp.setContentType("text/html");
		RythmEngine eng = RythmEngineFactory.getEngine(req, ctx.getRootDirectory());

		model.put("AppRoot", req.getSession().getServletContext().getContextPath());

		String filePath = ctx.getRootDirectory() + name + ctx.getSuffix();
		File file = new File(filePath);
		String template;
		if (!file.exists()) {
			LOG.info("NotExist=====================================================");
			template = IOUtils.toString(this.getClass().getClassLoader().getResource(filePath), "UTF-8");
		} else {
			LOG.info("Exist=====================================================");
			template = FileUtils.readFileToString(file, "UTF-8");
		}

		String s = eng.renderStr(template, model);
		resp.getWriter().write(s);
	}
}
