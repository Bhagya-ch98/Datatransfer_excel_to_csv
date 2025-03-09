package com.bhagya.Controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class XmlToJsonController {

	private static final String JSON_FILE_PATH = "D:\\DataTransformExcelandXml\\bhagya\\output.json";

	@GetMapping("/upload-xml")
	public String getfiles() {
		return "Xmlfileaccept";
	}

	@PostMapping("/convertXmlToJson")
	public String convertXmlToJson(@RequestParam("file") MultipartFile file, Model model) {
		if (file.isEmpty()) {
			model.addAttribute("message", "Please upload an XML file.");
			return "Xmlfileaccept";
		}

		try {
			XmlMapper xmlMapper = new XmlMapper();
			JsonNode jsonNode = xmlMapper.readTree(file.getInputStream());

			ObjectMapper jsonMapper = new ObjectMapper();
			String jsonString = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

			jsonMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE_PATH), jsonNode);

			model.addAttribute("jsonData", jsonString);
			model.addAttribute("message", "File converted successfully!");

		} catch (IOException e) {
			model.addAttribute("message", "Error processing file: " + e.getMessage());
		}

		return "Xmlfileaccept"; 
	}
}
