package com.pichincha.spfmsaapexcoreservice.service.impl;

import com.pichincha.spfmsaapexcoreservice.model.ReportDTO;
import com.pichincha.spfmsaapexcoreservice.service.PdfReportService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class PdfReportServiceImpl implements PdfReportService {

    @Override
    public byte[] generateAccountStatementPdf(List<ReportDTO> reportData) {
        try {
            // Agrupar datos por cuenta
            Map<String, List<ReportDTO>> groupedByAccount = groupByAccount(reportData);
            
            JasperDesign jasperDesign = createReportDesign();
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            // Preparar parámetros
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "ESTADO DE CUENTA BANCARIO");
            
            if (!reportData.isEmpty()) {
                parameters.put("ClientName", reportData.get(0).getClient());
                
                // Calcular totales generales
                double totalInitialBalance = groupedByAccount.values().stream()
                    .mapToDouble(list -> {
                        if (list.isEmpty()) return 0.0;
                        Double v = list.get(0).getInitialBalance();
                        return v != null ? v : 0.0;
                    })
                    .sum();
                
                double totalDeposits = reportData.stream()
                    .mapToDouble(r -> {
                        Double mv = r.getMovement();
                        return (mv != null && mv.doubleValue() > 0.0) ? mv.doubleValue() : 0.0;
                    })
                    .sum();

                double totalWithdrawals = Math.abs(reportData.stream()
                    .mapToDouble(r -> {
                        Double mv = r.getMovement();
                        return (mv != null && mv.doubleValue() < 0.0) ? mv.doubleValue() : 0.0;
                    })
                    .sum());

                double totalFinalBalance = reportData.stream()
                    .mapToDouble(r -> {
                        Double av = r.getAvailableBalance();
                        return av != null ? av.doubleValue() : 0.0;
                    })
                    .max()
                    .orElse(0.0);
                
                parameters.put("TotalAccounts", groupedByAccount.size());
                parameters.put("TotalInitialBalance", totalInitialBalance);
                parameters.put("TotalDeposits", totalDeposits);
                parameters.put("TotalWithdrawals", totalWithdrawals);
                parameters.put("TotalFinalBalance", totalFinalBalance);
            }
            
            // Crear datasource con datos agrupados y convertidos
            List<Map<String, Object>> convertedData = convertGroupedDataToMap(groupedByAccount);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(convertedData);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            
            return outputStream.toByteArray();
            
        } catch (JRException exception) {
            throw new RuntimeException("Error generating PDF report: " + exception.getMessage(), exception);
        }
    }
    
    private Map<String, List<ReportDTO>> groupByAccount(List<ReportDTO> reportData) {
        Map<String, List<ReportDTO>> grouped = new LinkedHashMap<>();
        for (ReportDTO report : reportData) {
            String accountKey = report.getAccountNumber() + "-" + report.getType();
            grouped.computeIfAbsent(accountKey, k -> new ArrayList<>()).add(report);
        }
        return grouped;
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
        
        // Parámetros para resumen general
        addParameter(jasperDesign, "TotalAccounts", Integer.class);
        addParameter(jasperDesign, "TotalInitialBalance", Double.class);
        addParameter(jasperDesign, "TotalDeposits", Double.class);
        addParameter(jasperDesign, "TotalWithdrawals", Double.class);
        addParameter(jasperDesign, "TotalFinalBalance", Double.class);
    }
    
    private void addParameter(JasperDesign jasperDesign, String name, Class<?> valueClass) throws JRException {
        JRDesignParameter parameter = new JRDesignParameter();
        parameter.setName(name);
        parameter.setValueClass(valueClass);
        jasperDesign.addParameter(parameter);
    }
    
    private void addFields(JasperDesign jasperDesign) throws JRException {
        // Campos para encabezados de cuenta
        addField(jasperDesign, "isAccountHeader", Boolean.class);
        addField(jasperDesign, "accountNumber", String.class);
        addField(jasperDesign, "accountType", String.class);
        addField(jasperDesign, "client", String.class);
        addField(jasperDesign, "accountInitialBalance", Double.class);
        addField(jasperDesign, "accountDeposits", Double.class);
        addField(jasperDesign, "accountWithdrawals", Double.class);
        addField(jasperDesign, "accountFinalBalance", Double.class);
        
        // Campos para movimientos
        addField(jasperDesign, "date", String.class);
        addField(jasperDesign, "type", String.class);
        addField(jasperDesign, "initialBalance", Double.class);
        addField(jasperDesign, "status", String.class);
        addField(jasperDesign, "movementType", String.class); // NUEVA COLUMNA
        addField(jasperDesign, "movement", Double.class);
        addField(jasperDesign, "availableBalance", Double.class);
    }
    
    private void addField(JasperDesign jasperDesign, String fieldName, Class<?> fieldClass) throws JRException {
        JRDesignField field = new JRDesignField();
        field.setName(fieldName);
        field.setValueClass(fieldClass);
        jasperDesign.addField(field);
    }
    
    private void addTitleBand(JasperDesign jasperDesign) {
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(260); // Aumentado para incluir resumen
        
        // Logo y nombre del banco NEXUS
        JRDesignStaticText bankName = new JRDesignStaticText();
        bankName.setX(0);
        bankName.setY(5);
        bankName.setWidth(515);
        bankName.setHeight(35);
        bankName.setText("BANCO NEXUS");
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
        bankSlogan.setHeight(12);
        bankSlogan.setText("Institución Financiera de Primera Categoría | Superintendencia de Bancos");
        bankSlogan.setFontSize(8f);
        bankSlogan.setItalic(true);
        bankSlogan.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        bankSlogan.setForecolor(new Color(100, 100, 100));
        titleBand.addElement(bankSlogan);
        
        // Línea separadora
        JRDesignLine line1 = new JRDesignLine();
        line1.setX(0);
        line1.setY(55);
        line1.setWidth(515);
        line1.setHeight(0);
        line1.setForecolor(new Color(0, 51, 102));
        titleBand.addElement(line1);
        
        // Título del documento
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setX(0);
        titleText.setY(62);
        titleText.setWidth(515);
        titleText.setHeight(20);
        titleText.setText("ESTADO DE CUENTA BANCARIO");
        titleText.setFontSize(14f);
        titleText.setBold(true);
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleBand.addElement(titleText);
        
        // Información del cliente
        JRDesignRectangle clientBox = new JRDesignRectangle();
        clientBox.setX(0);
        clientBox.setY(87);
        clientBox.setWidth(515);
        clientBox.setHeight(30);
        clientBox.setBackcolor(new Color(240, 248, 255));
        clientBox.setForecolor(new Color(0, 51, 102));
        titleBand.addElement(clientBox);
        
        JRDesignTextField clientField = new JRDesignTextField();
        clientField.setX(10);
        clientField.setY(92);
        clientField.setWidth(300);
        clientField.setHeight(12);
        clientField.setExpression(createExpression("\"Cliente: \" + $P{ClientName}"));
        clientField.setFontSize(10f);
        clientField.setBold(true);
        titleBand.addElement(clientField);
        
        // Fecha de generación
        JRDesignTextField dateField = new JRDesignTextField();
        dateField.setX(320);
        dateField.setY(92);
        dateField.setWidth(190);
        dateField.setHeight(12);
        dateField.setExpression(createExpression("\"Fecha: \" + new java.text.SimpleDateFormat(\"dd/MM/yyyy HH:mm:ss\").format(new java.util.Date())"));
        dateField.setFontSize(9f);
        dateField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        titleBand.addElement(dateField);
        
        JRDesignTextField accountCountField = new JRDesignTextField();
        accountCountField.setX(10);
        accountCountField.setY(104);
        accountCountField.setWidth(300);
        accountCountField.setHeight(12);
        accountCountField.setExpression(createExpression("\"Total de Cuentas: \" + $P{TotalAccounts}"));
        accountCountField.setFontSize(9f);
        titleBand.addElement(accountCountField);
        
        // RESUMEN GENERAL DE TODAS LAS CUENTAS
        JRDesignStaticText summaryTitle = new JRDesignStaticText();
        summaryTitle.setX(0);
        summaryTitle.setY(125);
        summaryTitle.setWidth(515);
        summaryTitle.setHeight(15);
        summaryTitle.setText("📊 RESUMEN GENERAL DE TODAS LAS CUENTAS");
        summaryTitle.setFontSize(11f);
        summaryTitle.setBold(true);
        summaryTitle.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        summaryTitle.setForecolor(new Color(0, 51, 102));
        titleBand.addElement(summaryTitle);
        
        // Fondo del resumen
        JRDesignRectangle summaryBox = new JRDesignRectangle();
        summaryBox.setX(0);
        summaryBox.setY(143);
        summaryBox.setWidth(515);
        summaryBox.setHeight(110);
        summaryBox.setBackcolor(new Color(245, 250, 255));
        summaryBox.setForecolor(new Color(0, 51, 102));
        titleBand.addElement(summaryBox);
        
        // Primera fila de resumen
        int yPos = 150;
        addSummaryField(titleBand, "Saldo Inicial Total:", "$P{TotalInitialBalance}", 10, yPos, 150, new Color(0, 51, 102));
        addSummaryField(titleBand, "Total Depósitos:", "$P{TotalDeposits}", 180, yPos, 150, new Color(0, 128, 0));
        addSummaryField(titleBand, "Total Retiros:", "$P{TotalWithdrawals}", 350, yPos, 150, new Color(200, 0, 0));
        
        // Segunda fila
        yPos += 25;
        addSummaryField(titleBand, "Saldo Final Total:", "$P{TotalFinalBalance}", 10, yPos, 150, new Color(0, 51, 102));
        
        // Nota informativa
        JRDesignStaticText noteText = new JRDesignStaticText();
        noteText.setX(10);
        noteText.setY(230);
        noteText.setWidth(495);
        noteText.setHeight(20);
        noteText.setText("El siguiente detalle muestra el movimiento consolidado de todas sus cuentas durante el período consultado.");
        noteText.setFontSize(7f);
        noteText.setItalic(true);
        noteText.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        noteText.setForecolor(new Color(100, 100, 100));
        titleBand.addElement(noteText);
        
        jasperDesign.setTitle(titleBand);
    }
    
    private void addSummaryField(JRDesignBand band, String label, String expression, int x, int y, int width, Color color) {
        // Etiqueta
        JRDesignStaticText labelText = new JRDesignStaticText();
        labelText.setX(x);
        labelText.setY(y);
        labelText.setWidth(width);
        labelText.setHeight(10);
        labelText.setText(label);
        labelText.setFontSize(8f);
        labelText.setBold(true);
        band.addElement(labelText);
        
        // Valor
        JRDesignTextField valueField = new JRDesignTextField();
        valueField.setX(x);
        valueField.setY(y + 10);
        valueField.setWidth(width);
        valueField.setHeight(12);
        valueField.setExpression(createExpression("\"$\" + new java.text.DecimalFormat(\"#,##0.00\").format(" + expression + ")"));
        valueField.setFontSize(10f);
        valueField.setBold(true);
        valueField.setForecolor(color);
        band.addElement(valueField);
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
        
        // ACTUALIZADO: Agregada columna "Tipo Movimiento"
        String[] headers = {"Fecha", "Cliente", "Nro.Cuenta", "Tipo", "Saldo Inicial", "Estado", "Tipo Movimiento", "Movimiento", "Saldo Disponible"};
        int[] widths = {50, 70, 60, 55, 60, 35, 70, 55, 60};
        int xPosition = 0;
        
        for (int i = 0; i < headers.length; i++) {
            JRDesignStaticText headerText = new JRDesignStaticText();
            headerText.setX(xPosition);
            headerText.setY(5);
            headerText.setWidth(widths[i]);
            headerText.setHeight(20);
            headerText.setText(headers[i]);
            headerText.setFontSize(7f); // Reducido para que quepa todo
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
        detailBand.setHeight(80); // Altura aumentada para encabezados con resumen completo
        
        int[] widths = {50, 70, 60, 55, 60, 35, 70, 55, 60};
        
        // ============ SECCIÓN 1: ENCABEZADO DE CUENTA ============
        // Solo se muestra cuando isAccountHeader = true
        
        // Línea separadora superior
        JRDesignLine accountSeparator = new JRDesignLine();
        accountSeparator.setX(0);
        accountSeparator.setY(0);
        accountSeparator.setWidth(515);
        accountSeparator.setHeight(0);
        accountSeparator.setForecolor(new Color(0, 51, 102));
        accountSeparator.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(accountSeparator);
        
        // Fondo del encabezado de cuenta (solo cuando es encabezado)
        JRDesignRectangle accountHeaderBox = new JRDesignRectangle();
        accountHeaderBox.setX(0);
        accountHeaderBox.setY(0);
        accountHeaderBox.setWidth(515);
        accountHeaderBox.setHeight(80);
        accountHeaderBox.setBackcolor(new Color(240, 248, 255));
        accountHeaderBox.setForecolor(new Color(0, 51, 102));
        accountHeaderBox.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(accountHeaderBox);
        
        // Título: "CUENTA AHORROS - 478758" (en MAYÚSCULAS)
        JRDesignTextField accountTitle = new JRDesignTextField();
        accountTitle.setX(10);
        accountTitle.setY(5);
        accountTitle.setWidth(495);
        accountTitle.setHeight(12);
        accountTitle.setExpression(createExpression("\"CUENTA \" + ($F{accountType} != null ? $F{accountType}.toUpperCase() : \"\") + \" - \" + ($F{accountNumber} != null ? $F{accountNumber} : \"\")"));
        accountTitle.setFontSize(10f);
        accountTitle.setBold(true);
        accountTitle.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        accountTitle.setForecolor(new Color(0, 51, 102));
        accountTitle.setBlankWhenNull(false);
        accountTitle.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(accountTitle);
        
        // Cliente de la cuenta (Titular) - Más prominente
        JRDesignTextField accountClient = new JRDesignTextField();
        accountClient.setX(10);
        accountClient.setY(19);
        accountClient.setWidth(495);
        accountClient.setHeight(12);
        accountClient.setExpression(createExpression("\"Cliente: \" + ($F{client} != null ? $F{client} : \"\")"));
        accountClient.setFontSize(9f);
        accountClient.setBold(true);
        accountClient.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        accountClient.setForecolor(new Color(51, 51, 51));
        accountClient.setBlankWhenNull(false);
        accountClient.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(accountClient);
        
        // Línea separadora entre cliente y resumen
        JRDesignLine summaryTopLine = new JRDesignLine();
        summaryTopLine.setX(10);
        summaryTopLine.setY(34);
        summaryTopLine.setWidth(495);
        summaryTopLine.setHeight(0);
        summaryTopLine.setForecolor(new Color(200, 200, 200));
        summaryTopLine.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(summaryTopLine);
        
        // Resumen de la cuenta (4 campos en línea) - DEBAJO DEL NOMBRE DEL CLIENTE
        // Primera línea: Etiquetas
        int labelY = 37;
        int valueY = 48;
        
        // Etiqueta: Saldo Inicial
        JRDesignStaticText labelInitial = new JRDesignStaticText();
        labelInitial.setX(10);
        labelInitial.setY(labelY);
        labelInitial.setWidth(120);
        labelInitial.setHeight(10);
        labelInitial.setText("Saldo Inicial:");
        labelInitial.setFontSize(8f);
        labelInitial.setBold(true);
        labelInitial.setForecolor(new Color(0, 0, 0));
        labelInitial.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(labelInitial);
        
        // Valor: Saldo Inicial
        JRDesignTextField accInitialBalance = new JRDesignTextField();
        accInitialBalance.setX(10);
        accInitialBalance.setY(valueY);
        accInitialBalance.setWidth(120);
        accInitialBalance.setHeight(14);
        accInitialBalance.setExpression(createExpression("\"$\" + new java.text.DecimalFormat(\"#,##0.00\").format($F{accountInitialBalance} != null ? $F{accountInitialBalance} : 0.0)"));
        accInitialBalance.setFontSize(10f);
        accInitialBalance.setBold(true);
        accInitialBalance.setForecolor(new Color(0, 51, 102));
        accInitialBalance.setBlankWhenNull(false);
        accInitialBalance.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(accInitialBalance);
        
        // Etiqueta: Total Depósitos
        JRDesignStaticText labelDeposits = new JRDesignStaticText();
        labelDeposits.setX(135);
        labelDeposits.setY(labelY);
        labelDeposits.setWidth(115);
        labelDeposits.setHeight(10);
        labelDeposits.setText("Total Depósitos:");
        labelDeposits.setFontSize(8f);
        labelDeposits.setBold(true);
        labelDeposits.setForecolor(new Color(0, 0, 0));
        labelDeposits.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(labelDeposits);
        
        // Valor: Total Depósitos
        JRDesignTextField accDeposits = new JRDesignTextField();
        accDeposits.setX(135);
        accDeposits.setY(valueY);
        accDeposits.setWidth(115);
        accDeposits.setHeight(14);
        accDeposits.setExpression(createExpression("\"$\" + new java.text.DecimalFormat(\"#,##0.00\").format($F{accountDeposits} != null ? $F{accountDeposits} : 0.0)"));
        accDeposits.setFontSize(10f);
        accDeposits.setBold(true);
        accDeposits.setForecolor(new Color(0, 128, 0));
        accDeposits.setBlankWhenNull(false);
        accDeposits.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(accDeposits);
        
        // Etiqueta: Total Retiros
        JRDesignStaticText labelWithdrawals = new JRDesignStaticText();
        labelWithdrawals.setX(255);
        labelWithdrawals.setY(labelY);
        labelWithdrawals.setWidth(115);
        labelWithdrawals.setHeight(10);
        labelWithdrawals.setText("Total Retiros:");
        labelWithdrawals.setFontSize(8f);
        labelWithdrawals.setBold(true);
        labelWithdrawals.setForecolor(new Color(0, 0, 0));
        labelWithdrawals.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(labelWithdrawals);
        
        // Valor: Total Retiros
        JRDesignTextField accWithdrawals = new JRDesignTextField();
        accWithdrawals.setX(255);
        accWithdrawals.setY(valueY);
        accWithdrawals.setWidth(115);
        accWithdrawals.setHeight(14);
        accWithdrawals.setExpression(createExpression("\"$\" + new java.text.DecimalFormat(\"#,##0.00\").format($F{accountWithdrawals} != null ? $F{accountWithdrawals} : 0.0)"));
        accWithdrawals.setFontSize(10f);
        accWithdrawals.setBold(true);
        accWithdrawals.setForecolor(new Color(200, 0, 0));
        accWithdrawals.setBlankWhenNull(false);
        accWithdrawals.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(accWithdrawals);
        
        // Etiqueta: Saldo Final
        JRDesignStaticText labelFinal = new JRDesignStaticText();
        labelFinal.setX(375);
        labelFinal.setY(labelY);
        labelFinal.setWidth(130);
        labelFinal.setHeight(10);
        labelFinal.setText("Saldo Final:");
        labelFinal.setFontSize(8f);
        labelFinal.setBold(true);
        labelFinal.setForecolor(new Color(0, 0, 0));
        labelFinal.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(labelFinal);
        
        // Valor: Saldo Final
        JRDesignTextField accFinalBalance = new JRDesignTextField();
        accFinalBalance.setX(375);
        accFinalBalance.setY(valueY);
        accFinalBalance.setWidth(130);
        accFinalBalance.setHeight(14);
        accFinalBalance.setExpression(createExpression("\"$\" + new java.text.DecimalFormat(\"#,##0.00\").format($F{accountFinalBalance} != null ? $F{accountFinalBalance} : 0.0)"));
        accFinalBalance.setFontSize(10f);
        accFinalBalance.setBold(true);
        accFinalBalance.setForecolor(new Color(0, 51, 102));
        accFinalBalance.setBlankWhenNull(false);
        accFinalBalance.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(accFinalBalance);
        
        // Línea separadora inferior del encabezado
        JRDesignLine accountSeparator2 = new JRDesignLine();
        accountSeparator2.setX(0);
        accountSeparator2.setY(79);
        accountSeparator2.setWidth(515);
        accountSeparator2.setHeight(0);
        accountSeparator2.setForecolor(new Color(0, 51, 102));
        accountSeparator2.setPrintWhenExpression(createExpression("Boolean.TRUE.equals($F{isAccountHeader})"));
        detailBand.addElement(accountSeparator2);
        
        // ============ SECCIÓN 2: FILA DE TRANSACCIÓN ============
        // Solo se muestra cuando isAccountHeader = false
        // IMPORTANTE: Y=0 para que no haya espacios entre transacciones
        
        // Fondo alternado para transacciones
        JRDesignRectangle rowBackground = new JRDesignRectangle();
        rowBackground.setX(0);
        rowBackground.setY(0);
        rowBackground.setWidth(515);
        rowBackground.setHeight(17);
        rowBackground.setBackcolor(new Color(250, 250, 250));
        rowBackground.setForecolor(new Color(200, 200, 200));
        rowBackground.setPrintWhenExpression(createExpression("new Boolean(!$F{isAccountHeader}.booleanValue())"));
        detailBand.addElement(rowBackground);
        
        // Línea inferior de cada fila de transacción
        JRDesignLine rowLine = new JRDesignLine();
        rowLine.setX(0);
        rowLine.setY(16);
        rowLine.setWidth(515);
        rowLine.setHeight(0);
        rowLine.setForecolor(new Color(220, 220, 220));
        rowLine.setPrintWhenExpression(createExpression("new Boolean(!$F{isAccountHeader}.booleanValue())"));
        detailBand.addElement(rowLine);
        
        int xPosition = 0;
        int transactionY = 2; // Posición Y=2 para centrar texto en fila de 17px
        
        // Fecha
        JRDesignTextField dateField = new JRDesignTextField();
        dateField.setX(xPosition);
        dateField.setY(transactionY);
        dateField.setWidth(widths[0]);
        dateField.setHeight(13);
        dateField.setExpression(createExpression("$F{date}"));
        dateField.setFontSize(7f);
        dateField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        dateField.setPrintWhenExpression(createExpression("new Boolean(!$F{isAccountHeader}.booleanValue())"));
        detailBand.addElement(dateField);
        xPosition += widths[0];
        
        // Cliente
        JRDesignTextField clientField = new JRDesignTextField();
        clientField.setX(xPosition);
        clientField.setY(transactionY);
        clientField.setWidth(widths[1]);
        clientField.setHeight(13);
        clientField.setExpression(createExpression("$F{client}"));
        clientField.setFontSize(7f);
        clientField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        clientField.setPrintWhenExpression(createExpression("new Boolean(!$F{isAccountHeader}.booleanValue())"));
        detailBand.addElement(clientField);
        xPosition += widths[1];
        
        // Número de cuenta
        JRDesignTextField accountNumberField = new JRDesignTextField();
        accountNumberField.setX(xPosition);
        accountNumberField.setY(transactionY);
        accountNumberField.setWidth(widths[2]);
        accountNumberField.setHeight(13);
        accountNumberField.setExpression(createExpression("$F{accountNumber}"));
        accountNumberField.setFontSize(7f);
        accountNumberField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        accountNumberField.setPrintWhenExpression(createExpression("new Boolean(!$F{isAccountHeader}.booleanValue())"));
        detailBand.addElement(accountNumberField);
        xPosition += widths[2];
        
        // Tipo de cuenta
        JRDesignTextField typeField = new JRDesignTextField();
        typeField.setX(xPosition);
        typeField.setY(transactionY);
        typeField.setWidth(widths[3]);
        typeField.setHeight(13);
        typeField.setExpression(createExpression("$F{type}"));
        typeField.setFontSize(7f);
        typeField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        typeField.setBold(true);
        typeField.setForecolor(new Color(0, 51, 102));
        typeField.setPrintWhenExpression(createExpression("new Boolean(!$F{isAccountHeader}.booleanValue())"));
        detailBand.addElement(typeField);
        xPosition += widths[3];
        
        // Saldo Inicial
        JRDesignTextField initialBalanceField = new JRDesignTextField();
        initialBalanceField.setX(xPosition);
        initialBalanceField.setY(transactionY);
        initialBalanceField.setWidth(widths[4]);
        initialBalanceField.setHeight(13);
        initialBalanceField.setExpression(createExpression("\"$\" + new java.text.DecimalFormat(\"#,##0.00\").format($F{initialBalance})"));
        initialBalanceField.setFontSize(7f);
        initialBalanceField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        initialBalanceField.setPrintWhenExpression(createExpression("new Boolean(!$F{isAccountHeader}.booleanValue())"));
        detailBand.addElement(initialBalanceField);
        xPosition += widths[4];
        
        // Estado
        JRDesignTextField statusField = new JRDesignTextField();
        statusField.setX(xPosition);
        statusField.setY(transactionY);
        statusField.setWidth(widths[5]);
        statusField.setHeight(13);
        statusField.setExpression(createExpression("$F{status}"));
        statusField.setFontSize(7f);
        statusField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        statusField.setPrintWhenExpression(createExpression("new Boolean(!$F{isAccountHeader}.booleanValue())"));
        detailBand.addElement(statusField);
        xPosition += widths[5];
        
        // Tipo de Movimiento (NUEVA COLUMNA CON EMOJIS)
        JRDesignTextField movementTypeField = new JRDesignTextField();
        movementTypeField.setX(xPosition);
        movementTypeField.setY(transactionY);
        movementTypeField.setWidth(widths[6]);
        movementTypeField.setHeight(13);
        movementTypeField.setExpression(createExpression("$F{movementType}"));
        movementTypeField.setFontSize(7f);
        movementTypeField.setBold(true);
        movementTypeField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        movementTypeField.setPrintWhenExpression(createExpression("new Boolean(!$F{isAccountHeader}.booleanValue())"));
        detailBand.addElement(movementTypeField);
        xPosition += widths[6];
        
        // Movimiento
        JRDesignTextField movementField = new JRDesignTextField();
        movementField.setX(xPosition);
        movementField.setY(transactionY);
        movementField.setWidth(widths[7]);
        movementField.setHeight(13);
        movementField.setExpression(createExpression("\"$\" + new java.text.DecimalFormat(\"#,##0.00\").format($F{movement})"));
        movementField.setFontSize(7f);
        movementField.setBold(true);
        movementField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        movementField.setPrintWhenExpression(createExpression("new Boolean(!$F{isAccountHeader}.booleanValue())"));
        detailBand.addElement(movementField);
        xPosition += widths[7];
        
        // Saldo Disponible
        JRDesignTextField availableBalanceField = new JRDesignTextField();
        availableBalanceField.setX(xPosition);
        availableBalanceField.setY(transactionY);
        availableBalanceField.setWidth(widths[8]);
        availableBalanceField.setHeight(13);
        availableBalanceField.setExpression(createExpression("\"$\" + new java.text.DecimalFormat(\"#,##0.00\").format($F{availableBalance})"));
        availableBalanceField.setFontSize(7f);
        availableBalanceField.setBold(true);
        availableBalanceField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        availableBalanceField.setForecolor(new Color(0, 100, 0));
        availableBalanceField.setPrintWhenExpression(createExpression("new Boolean(!$F{isAccountHeader}.booleanValue())"));
        detailBand.addElement(availableBalanceField);
        
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
    
    private List<Map<String, Object>> convertGroupedDataToMap(Map<String, List<ReportDTO>> groupedByAccount) {
        DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Map<String, Object>> dataList = new ArrayList<>();
        
        for (Map.Entry<String, List<ReportDTO>> entry : groupedByAccount.entrySet()) {
            List<ReportDTO> accountReports = entry.getValue();
            
            if (accountReports.isEmpty()) continue;
            
            // Agregar encabezado de cuenta (primera fila)
            ReportDTO firstReport = accountReports.get(0);
            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("isAccountHeader", true);
            headerMap.put("accountNumber", firstReport.getAccountNumber());
            headerMap.put("accountType", translateAccountType(firstReport.getType()));
            headerMap.put("client", firstReport.getClient());
            
            // Calcular totales de esta cuenta
            // Usar Double para evitar auto-unboxing en tiempo de compilación
            Double accountInitialBalance = firstReport.getInitialBalance() != null ? firstReport.getInitialBalance() : Double.valueOf(0.0);
            double accDepositsD = accountReports.stream()
                .mapToDouble(r -> {
                    Double mv = r.getMovement();
                    return (mv != null && mv.doubleValue() > 0.0) ? mv.doubleValue() : 0.0;
                })
                .sum();
            Double accountDeposits = Double.valueOf(accDepositsD);

            double accWithdrawalsD = Math.abs(accountReports.stream()
                .mapToDouble(r -> {
                    Double mv = r.getMovement();
                    return (mv != null && mv.doubleValue() < 0.0) ? mv.doubleValue() : 0.0;
                })
                .sum());
            Double accountWithdrawals = Double.valueOf(accWithdrawalsD);

            double accFinalD = accountReports.stream()
                .mapToDouble(r -> {
                    Double av = r.getAvailableBalance();
                    return av != null ? av.doubleValue() : 0.0;
                })
                .max()
                .orElse(0.0);
            Double accountFinalBalance = Double.valueOf(accFinalD);

            headerMap.put("accountInitialBalance", accountInitialBalance);
            headerMap.put("accountDeposits", accountDeposits);
            headerMap.put("accountWithdrawals", accountWithdrawals);
            headerMap.put("accountFinalBalance", accountFinalBalance);
            
            // Log de depuración
            log.info("=== ENCABEZADO DE CUENTA ===");
            log.info("Cuenta: {} - {}", firstReport.getAccountNumber(), firstReport.getType());
            log.info("Cliente: {}", firstReport.getClient());
            log.info("Saldo Inicial: {}", accountInitialBalance);
            log.info("Total Depósitos: {}", accountDeposits);
            log.info("Total Retiros: {}", accountWithdrawals);
            log.info("Saldo Final: {}", accountFinalBalance);
            
            // Agregar campos vacíos para los campos de transacción (requeridos por JasperReports)
            headerMap.put("date", "");
            headerMap.put("type", "");
            headerMap.put("initialBalance", 0.0);
            headerMap.put("status", "");
            headerMap.put("movementType", "");
            headerMap.put("movement", 0.0);
            headerMap.put("availableBalance", 0.0);
            
            dataList.add(headerMap);
            
            // Agregar movimientos de la cuenta
            for (ReportDTO report : accountReports) {
                Map<String, Object> map = new HashMap<>();
                map.put("isAccountHeader", false);
                
                // Campos de encabezado de cuenta (vacíos para transacciones)
                map.put("accountInitialBalance", 0.0);
                map.put("accountDeposits", 0.0);
                map.put("accountWithdrawals", 0.0);
                map.put("accountFinalBalance", 0.0);
                map.put("accountType", "");
                
                java.time.LocalDate localDate = report.getDate();
                if (localDate != null) {
                    String fecha = localDate.format(dateOnlyFormatter);
                    map.put("date", fecha);
                } else {
                    map.put("date", "");
                }
                
                map.put("client", report.getClient() != null ? report.getClient() : "");
                map.put("accountNumber", report.getAccountNumber() != null ? report.getAccountNumber() : "");
                map.put("type", translateAccountType(report.getType()));
                map.put("initialBalance", report.getInitialBalance() != null ? report.getInitialBalance() : Double.valueOf(0.0));
                map.put("status", Boolean.TRUE.equals(report.getStatus()) ? "Sí" : "No");
                
                // Tipo de movimiento con emojis
                String movementType = "";
                Double mvLocal = report.getMovement();
                if (mvLocal != null) {
                    movementType = mvLocal.doubleValue() >= 0.0 ? "🟢 DEPÓSITO" : "🔴 RETIRO";
                }
                map.put("movementType", movementType);
                
                map.put("movement", report.getMovement() != null ? report.getMovement() : Double.valueOf(0.0));
                map.put("availableBalance", report.getAvailableBalance() != null ? report.getAvailableBalance() : Double.valueOf(0.0));
                
                dataList.add(map);
            }
        }
        return dataList;
    }
    
    private String translateAccountType(String type) {
        if (type == null) return "";
        switch (type.toUpperCase()) {
            case "SAVINGS":
                return "Ahorros";
            case "CHECKING":
                return "Corriente";
            default:
                return type;
        }
    }
}
