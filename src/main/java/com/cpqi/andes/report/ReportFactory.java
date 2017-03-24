package com.cpqi.andes.report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class ReportFactory {
    
    private final Environment env;
    
    public ReportFactory(Environment environment) {
	this.env = environment;
    }
    
    @Autowired
    ServletContext ser;
    
    /**
     * @param fileName
     * @param parameters
     * @param dataSource
     * @return
     * @throws JRException
     * @throws SQLException
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public byte[] reportPDF(String fileName, Map<String, Object> parameters, DataSource dataSource) throws JRException, SQLException, IOException {
	
	String current = new java.io.File(".").getCanonicalPath();
	
	String jrxmlFileName = fileName + ".jrxml";
	String jasperFileName = fileName + ".jasper";
	String pdfFileName = fileName + ".pdf";

	JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);

	JasperPrint jprint = JasperFillManager.fillReport(jasperFileName, parameters, dataSource.getConnection());
	
	JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);

	byte[] pdfReportFileBytes = Files.readAllBytes(new File(pdfFileName).toPath());
	
	return pdfReportFileBytes;
	
    }
    
}
