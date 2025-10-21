package com.pichincha.spfmsaapexcoreservice.service.impl;

import com.pichincha.spfmsaapexcoreservice.model.ReportDTO;
import com.pichincha.spfmsaapexcoreservice.service.PdfReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PdfReportServiceImpl implements PdfReportService {

    @Override
    public byte[] generateAccountStatementPdf(List<ReportDTO> reportData) {
        try {
            JasperDesign jasperDesign = createReportDesign();
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                convertReportDTOToMap(reportData)
            );
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "Estado de Cuenta Bancario");
            
            if (!reportData.isEmpty()) {
                parameters.put("ClientName", reportData.get(0).getClient());
                parameters.put("AccountNumber", reportData.get(0).getAccountNumber());
            }
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            
            return outputStream.toByteArray();
            
        } catch (JRException exception) {
            throw new RuntimeException("Error generating PDF report: " + exception.getMessage(), exception);
        }
    }
    
    private JasperDesign createReportDesign() throws JRException {
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("AccountStatement");
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);
        jasperDesign.setColumnWidth(515);
        jasperDesign.setColumnSpacing(0);
        jasperDesign.setLeftMargin(40);
        jasperDesign.setRightMargin(40);
        jasperDesign.setTopMargin(50);
        jasperDesign.setBottomMargin(50);
        
        addParameters(jasperDesign);
        addFields(jasperDesign);
        addTitleBand(jasperDesign);
        addColumnHeaderBand(jasperDesign);
        addDetailBand(jasperDesign);
        addPageFooter(jasperDesign);
        
        return jasperDesign;
    }
    
    private void addParameters(JasperDesign jasperDesign) throws JRException {
        JRDesignParameter parameterTitle = new JRDesignParameter();
        parameterTitle.setName("ReportTitle");
        parameterTitle.setValueClass(String.class);
        jasperDesign.addParameter(parameterTitle);
        
        JRDesignParameter parameterClientName = new JRDesignParameter();
        parameterClientName.setName("ClientName");
        parameterClientName.setValueClass(String.class);
        jasperDesign.addParameter(parameterClientName);
        
        JRDesignParameter parameterAccountNumber = new JRDesignParameter();
        parameterAccountNumber.setName("AccountNumber");
        parameterAccountNumber.setValueClass(String.class);
        jasperDesign.addParameter(parameterAccountNumber);
    }
    
    private void addFields(JasperDesign jasperDesign) throws JRException {
        addField(jasperDesign, "date", String.class);
        addField(jasperDesign, "type", String.class);
        addField(jasperDesign, "initialBalance", Double.class);
        addField(jasperDesign, "movement", Double.class);
        addField(jasperDesign, "availableBalance", Double.class);
        addField(jasperDesign, "status", String.class);
    }
    
    private void addField(JasperDesign jasperDesign, String fieldName, Class<?> fieldClass) throws JRException {
        JRDesignField field = new JRDesignField();
        field.setName(fieldName);
        field.setValueClass(fieldClass);
        jasperDesign.addField(field);
    }
    
    private void addTitleBand(JasperDesign jasperDesign) {
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(140);
        
        // Logo y nombre del banco NEXUS
        JRDesignStaticText bankName = new JRDesignStaticText();
        bankName.setX(0);
        bankName.setY(5);
        bankName.setWidth(515);
        bankName.setHeight(35);
        bankName.setText("NEXUS");
        bankName.setFontSize(28f);
        bankName.setBold(true);
        bankName.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        bankName.setForecolor(new Color(0, 51, 102)); // Azul corporativo
        titleBand.addElement(bankName);
        
        // Subtítulo del banco
        JRDesignStaticText bankSlogan = new JRDesignStaticText();
        bankSlogan.setX(0);
        bankSlogan.setY(38);
        bankSlogan.setWidth(515);
        bankSlogan.setHeight(15);
        bankSlogan.setText("Tu banco de confianza");
        bankSlogan.setFontSize(10f);
        bankSlogan.setItalic(true);
        bankSlogan.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        bankSlogan.setForecolor(new Color(100, 100, 100));
        titleBand.addElement(bankSlogan);
        
        // Línea separadora
        JRDesignLine line1 = new JRDesignLine();
        line1.setX(0);
        line1.setY(58);
        line1.setWidth(515);
        line1.setHeight(0);
        line1.setForecolor(new Color(0, 51, 102));
        titleBand.addElement(line1);
        
        // Título del documento
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setX(0);
        titleText.setY(65);
        titleText.setWidth(515);
        titleText.setHeight(25);
        titleText.setText("ESTADO DE CUENTA BANCARIO");
        titleText.setFontSize(16f);
        titleText.setBold(true);
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleBand.addElement(titleText);
        
        // Información del cliente (con fondo)
        JRDesignRectangle clientBox = new JRDesignRectangle();
        clientBox.setX(0);
        clientBox.setY(95);
        clientBox.setWidth(515);
        clientBox.setHeight(40);
        clientBox.setBackcolor(new Color(240, 248, 255)); // Azul muy claro
        clientBox.setForecolor(new Color(0, 51, 102));
        titleBand.addElement(clientBox);
        
        JRDesignTextField clientField = new JRDesignTextField();
        clientField.setX(10);
        clientField.setY(100);
        clientField.setWidth(250);
        clientField.setHeight(15);
        clientField.setExpression(createExpression("\"Cliente: \" + $P{ClientName}"));
        clientField.setFontSize(11f);
        clientField.setBold(true);
        titleBand.addElement(clientField);
        
        JRDesignTextField accountField = new JRDesignTextField();
        accountField.setX(10);
        accountField.setY(115);
        accountField.setWidth(250);
        accountField.setHeight(15);
        accountField.setExpression(createExpression("\"Nº de Cuenta: \" + $P{AccountNumber}"));
        accountField.setFontSize(11f);
        clientField.setBold(true);
        titleBand.addElement(accountField);
        
    // Fecha de generación del reporte (con hora, minutos y segundos)
        JRDesignTextField dateField = new JRDesignTextField();
        dateField.setX(300);
        dateField.setY(100);
        dateField.setWidth(210);
        dateField.setHeight(15);
    dateField.setExpression(createExpression("\"Fecha: \" + new java.text.SimpleDateFormat(\"dd/MM/yyyy HH:mm:ss\").format(new java.util.Date())"));
        dateField.setFontSize(9f);
        dateField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        titleBand.addElement(dateField);
        
        jasperDesign.setTitle(titleBand);
    }
    
    private void addColumnHeaderBand(JasperDesign jasperDesign) {
        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(30);
        
        // Fondo azul para el encabezado
        JRDesignRectangle headerBackground = new JRDesignRectangle();
        headerBackground.setX(0);
        headerBackground.setY(0);
        headerBackground.setWidth(515);
        headerBackground.setHeight(30);
        headerBackground.setBackcolor(new Color(0, 51, 102)); // Azul corporativo
        headerBackground.setForecolor(new Color(0, 51, 102));
        columnHeaderBand.addElement(headerBackground);
        
        String[] headers = {"Fecha", "Tipo de Cuenta", "Saldo Inicial", "Movimiento", "Saldo Final", "Estado"};
        int[] widths = {70, 100, 85, 75, 85, 100};
        int xPosition = 0;
        
        for (int i = 0; i < headers.length; i++) {
            JRDesignStaticText headerText = new JRDesignStaticText();
            headerText.setX(xPosition);
            headerText.setY(5);
            headerText.setWidth(widths[i]);
            headerText.setHeight(20);
            headerText.setText(headers[i]);
            headerText.setFontSize(9f);
            headerText.setBold(true);
            headerText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            headerText.setForecolor(Color.WHITE); // Texto blanco
            columnHeaderBand.addElement(headerText);
            xPosition += widths[i];
        }
        
        jasperDesign.setColumnHeader(columnHeaderBand);
    }
    
    private void addDetailBand(JasperDesign jasperDesign) {
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(25);
        
        // Fondo alternado (se puede hacer con expresiones condicionales)
        JRDesignRectangle rowBackground = new JRDesignRectangle();
        rowBackground.setX(0);
        rowBackground.setY(0);
        rowBackground.setWidth(515);
        rowBackground.setHeight(25);
        rowBackground.setBackcolor(new Color(250, 250, 250));
        rowBackground.setForecolor(new Color(200, 200, 200));
        detailBand.addElement(rowBackground);
        
        // Línea inferior de cada fila
        JRDesignLine rowLine = new JRDesignLine();
        rowLine.setX(0);
        rowLine.setY(24);
        rowLine.setWidth(515);
        rowLine.setHeight(0);
        rowLine.setForecolor(new Color(220, 220, 220));
        detailBand.addElement(rowLine);
        
        int[] widths = {70, 100, 85, 75, 85, 100};
        int xPosition = 0;
        
        // Fecha
        JRDesignTextField dateField = new JRDesignTextField();
        dateField.setX(xPosition);
        dateField.setY(5);
        dateField.setWidth(widths[0]);
        dateField.setHeight(15);
        dateField.setExpression(createExpression("$F{date}"));
        dateField.setFontSize(8f);
        dateField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        detailBand.addElement(dateField);
        xPosition += widths[0];
        
        // Tipo de cuenta (TRADUCIDO)
        JRDesignTextField typeField = new JRDesignTextField();
        typeField.setX(xPosition);
        typeField.setY(5);
        typeField.setWidth(widths[1]);
        typeField.setHeight(15);
        typeField.setExpression(createExpression("$F{type}.equals(\"SAVINGS\") ? \"Ahorros\" : $F{type}.equals(\"CHECKING\") ? \"Corriente\" : $F{type}"));
        typeField.setFontSize(8f);
        typeField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        typeField.setBold(true);
        typeField.setForecolor(new Color(0, 51, 102));
        detailBand.addElement(typeField);
        xPosition += widths[1];
        
        // Saldo Inicial
        JRDesignTextField initialBalanceField = new JRDesignTextField();
        initialBalanceField.setX(xPosition);
        initialBalanceField.setY(5);
        initialBalanceField.setWidth(widths[2]);
        initialBalanceField.setHeight(15);
        initialBalanceField.setExpression(createExpression("\"$\" + new java.text.DecimalFormat(\"#,##0.00\").format($F{initialBalance})"));
        initialBalanceField.setFontSize(8f);
        initialBalanceField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        detailBand.addElement(initialBalanceField);
        xPosition += widths[2];
        
        // Movimiento (con color según signo)
        JRDesignTextField movementField = new JRDesignTextField();
        movementField.setX(xPosition);
        movementField.setY(5);
        movementField.setWidth(widths[3]);
        movementField.setHeight(15);
        movementField.setExpression(createExpression("\"$\" + new java.text.DecimalFormat(\"#,##0.00\").format($F{movement})"));
        movementField.setFontSize(8f);
        movementField.setBold(true);
        movementField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        detailBand.addElement(movementField);
        xPosition += widths[3];
        
        // Saldo Final
        JRDesignTextField availableBalanceField = new JRDesignTextField();
        availableBalanceField.setX(xPosition);
        availableBalanceField.setY(5);
        availableBalanceField.setWidth(widths[4]);
        availableBalanceField.setHeight(15);
        availableBalanceField.setExpression(createExpression("\"$\" + new java.text.DecimalFormat(\"#,##0.00\").format($F{availableBalance})"));
        availableBalanceField.setFontSize(8f);
        availableBalanceField.setBold(true);
        availableBalanceField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        availableBalanceField.setForecolor(new Color(0, 100, 0));
        detailBand.addElement(availableBalanceField);
        xPosition += widths[4];
        
        // Estado
        JRDesignTextField statusField = new JRDesignTextField();
        statusField.setX(xPosition);
        statusField.setY(5);
        statusField.setWidth(widths[5]);
        statusField.setHeight(15);
        statusField.setExpression(createExpression("$F{status}"));
        statusField.setFontSize(8f);
        statusField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        detailBand.addElement(statusField);
        
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);
    }
    
    private void addPageFooter(JasperDesign jasperDesign) {
        JRDesignBand pageFooter = new JRDesignBand();
        pageFooter.setHeight(50);
        
        // Línea superior
        JRDesignLine topLine = new JRDesignLine();
        topLine.setX(0);
        topLine.setY(5);
        topLine.setWidth(515);
        topLine.setHeight(0);
        topLine.setForecolor(new Color(0, 51, 102));
        pageFooter.addElement(topLine);
        
        // Información del banco
        JRDesignStaticText footerInfo = new JRDesignStaticText();
        footerInfo.setX(0);
        footerInfo.setY(10);
        footerInfo.setWidth(350);
        footerInfo.setHeight(15);
        footerInfo.setText("NEXUS - Servicios Financieros | www.nexusbank.com | 1-800-NEXUS");
        footerInfo.setFontSize(7f);
        footerInfo.setForecolor(new Color(100, 100, 100));
        pageFooter.addElement(footerInfo);
        
        // Número de página
        JRDesignTextField pageNumber = new JRDesignTextField();
        pageNumber.setX(350);
        pageNumber.setY(10);
        pageNumber.setWidth(165);
        pageNumber.setHeight(15);
        pageNumber.setExpression(createExpression("\"Página \" + $V{PAGE_NUMBER}"));
        pageNumber.setFontSize(8f);
        pageNumber.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        pageFooter.addElement(pageNumber);
        
        // Advertencia legal
        JRDesignStaticText legalNote = new JRDesignStaticText();
        legalNote.setX(0);
        legalNote.setY(25);
        legalNote.setWidth(515);
        legalNote.setHeight(20);
        legalNote.setText("Este documento es confidencial. Para consultas, contacte con su asesor bancario.");
        legalNote.setFontSize(7f);
        legalNote.setItalic(true);
        legalNote.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        legalNote.setForecolor(new Color(150, 150, 150));
        pageFooter.addElement(legalNote);
        
        jasperDesign.setPageFooter(pageFooter);
    }
    
    private JRDesignExpression createExpression(String text) {
        JRDesignExpression expression = new JRDesignExpression();
        expression.setText(text);
        return expression;
    }
    
    private List<Map<String, Object>> convertReportDTOToMap(List<ReportDTO> reportData) {
    DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<Map<String, Object>> dataList = new ArrayList<>();
        for (ReportDTO report : reportData) {
            Map<String, Object> map = new HashMap<>();
            // ReportDTO.generated uses LocalDate for date. Si quieres tiempos reales, cambia el DTO a LocalDateTime/OffsetDateTime.
            java.time.LocalDate localDate = report.getDate();
            if (localDate != null) {
                // Formatear la fecha y añadir hora por defecto 00:00:00 (si no hay hora en la fuente)
                String fecha = localDate.format(dateOnlyFormatter) + " 00:00:00";
                map.put("date", fecha);
            } else {
                map.put("date", "");
            }

            map.put("type", report.getType() != null ? report.getType() : "");
            map.put("initialBalance", report.getInitialBalance() != null ? report.getInitialBalance() : Double.valueOf(0.0));
            map.put("movement", report.getMovement() != null ? report.getMovement() : Double.valueOf(0.0));
            map.put("availableBalance", report.getAvailableBalance() != null ? report.getAvailableBalance() : Double.valueOf(0.0));
            map.put("status", Boolean.TRUE.equals(report.getStatus()) ? "Activo" : "Inactivo");
            dataList.add(map);
        }
        return dataList;
    }
}
