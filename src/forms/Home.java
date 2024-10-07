package forms;

import com.mysql.jdbc.RowData;
import com.raven.datechooser.DateBetween;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.listener.DateChooserAction;
import com.raven.datechooser.listener.DateChooserAdapter;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Employee;
import model.Person;
import model.Leave;
//import model.PaySlip;
//import model.Payroll;
import model.Print;
import model.SingletonConnection;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.awt.print.*;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.table.TableRowSorter;
import model.SQLRun;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.text.MessageFormat;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;
import model.DbConnection;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;

    class CheckboxCellRenderer extends JCheckBox implements TableCellRenderer {
        public CheckboxCellRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Boolean) {
                setSelected((Boolean) value);
            }
            return this;
        }
    }

    class LogManager {

    private static final Logger logger = Logger.getLogger(LogManager.class.getName());

    static {
        try {
            // Create a FileHandler that writes log messages to a file
            FileHandler fileHandler = new FileHandler("application.log", true);
            fileHandler.setFormatter(new CustomFormatter()); // Use the custom formatter
            logger.addHandler(fileHandler);

            // Set the logger level to ALL, to log all messages
            logger.setLevel(java.util.logging.Level.ALL);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}

class CustomFormatter extends Formatter {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(dateFormat.format(new Date(record.getMillis())))
          .append(" ")
          .append(record.getLevel().getName())
          .append(" ")
          .append(formatMessage(record))
          .append(System.lineSeparator());
        return sb.toString();
    }
}

    

public class Home extends javax.swing.JFrame {
//Creating Objects

    private static final Logger logger = LogManager.getLogger();
    private DateChooser chDate2 = new DateChooser();
   
    private DateChooser newempbday = new DateChooser();
    private DateChooser newempdatehired = new DateChooser();
    private DateChooser newempresignation = new DateChooser();
    private DateChooser newemplastdayofwork = new DateChooser();
    private DateChooser newempdateofawol = new DateChooser();
    private DateChooser empupdtbday = new DateChooser();
    private DateChooser empupdtdatehired = new DateChooser();
    private DateChooser empupdtresignation = new DateChooser();
    private DateChooser empupdtlastdayofwork = new DateChooser();
    private DateChooser empupdtdateofawol = new DateChooser();
    private DateChooser empupdtcontractstart = new DateChooser();
    private DateChooser empupdtcontractend = new DateChooser();
    private DefaultTableModel model;
    private DefaultTableModel model2;
    private DefaultTableModel model3;
    private DefaultTableModel contractEndTableModel;
    private DefaultTableModel contractExpiredTableModel;
    private DefaultTableModel fourMonthsTableModel;
    //private DefaultTableModel searchEmployee;
    DefaultTableModel searchEmployee = new DefaultTableModel() {
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 24 || columnIndex == 25 || columnIndex == 26) { // Assuming the 27th column is for checkboxes
            return Boolean.class;
        }
        return super.getColumnClass(columnIndex);
    }
};
    
    JTable empDetails;
    Employee objEmployee = new Employee();
    Leave objLeave = new Leave();
    Person objPerson = new Person();
    JFrame frame = new JFrame("Confirmation Dialog Example");

    PrinterJob printJob = PrinterJob.getPrinterJob();

    BufferedImage image1;
    BufferedImage image2;
    
    int id;
    int monthsCompleted;

    /**
     * Creates new form Home
     */
    public Home() {

        initComponents();
        addButtonGroup();
        changeIcon();
        
        jTable4.getColumnModel().getColumn(23).setCellRenderer(new CheckboxCellRenderer());
        jTable4.getColumnModel().getColumn(24).setCellRenderer(new CheckboxCellRenderer());
        jTable4.getColumnModel().getColumn(25).setCellRenderer(new CheckboxCellRenderer());
        
        newempbday.setTextField(txt_bday_newemp);
        newempbday.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        newempbday.setLabelCurrentDayVisible(false);
        newempbday.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_bday_newemp.setText(null);
        
        newempdatehired.setTextField(txt_datehired_newemp);
        newempdatehired.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        newempdatehired.setLabelCurrentDayVisible(false);
        newempdatehired.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_datehired_newemp.setText(null);
        
        newempresignation.setTextField(txt_resignation_newemp);
        newempresignation.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        newempresignation.setLabelCurrentDayVisible(false);
        newempresignation.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_resignation_newemp.setText(null);
        
        newemplastdayofwork.setTextField(txt_lastdayofwork_newemp);
        newemplastdayofwork.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        newemplastdayofwork.setLabelCurrentDayVisible(false);
        newemplastdayofwork.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_lastdayofwork_newemp.setText(null);
        
        newempdateofawol.setTextField(txt_dateofawol_newemp);
        newempdateofawol.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        newempdateofawol.setLabelCurrentDayVisible(false);
        newempdateofawol.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_dateofawol_newemp.setText(null);
        
        empupdtbday.setTextField(txt_bday_empupdt);
        empupdtbday.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        empupdtbday.setLabelCurrentDayVisible(false);
        empupdtbday.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_bday_empupdt.setText(null);
        
        empupdtdatehired.setTextField(txt_datehired_empupdt);
        empupdtdatehired.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        empupdtdatehired.setLabelCurrentDayVisible(false);
        empupdtdatehired.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_datehired_empupdt.setText(null);
        
        empupdtresignation.setTextField(txt_resignation_empupdt);
        empupdtresignation.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        empupdtresignation.setLabelCurrentDayVisible(false);
        empupdtresignation.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_resignation_empupdt.setText(null);
        
        empupdtlastdayofwork.setTextField(txt_lastdayofwork_empupdt);
        empupdtlastdayofwork.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        empupdtlastdayofwork.setLabelCurrentDayVisible(false);
        empupdtlastdayofwork.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_lastdayofwork_empupdt.setText(null);
        
        empupdtdateofawol.setTextField(txt_dateofawol_empupdt);
        empupdtdateofawol.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        empupdtdateofawol.setLabelCurrentDayVisible(false);
        empupdtdateofawol.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_dateofawol_empupdt.setText(null);
        
        empupdtcontractstart.setTextField(txt_contractstart_empupdt);
        empupdtcontractstart.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        empupdtcontractstart.setLabelCurrentDayVisible(false);
        empupdtcontractstart.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_contractstart_empupdt.setText(null);
        
        empupdtcontractend.setTextField(txt_contractend_empupdt);
        empupdtcontractend.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        empupdtcontractend.setLabelCurrentDayVisible(false);
        empupdtcontractend.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        txt_contractend_empupdt.setText(null);

        //chDate2.setTextField(txtDate2);
        chDate2.setDateSelectionMode(DateChooser.DateSelectionMode.BETWEEN_DATE_SELECTED);
        chDate2.setLabelCurrentDayVisible(false);
        chDate2.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        //model = (DefaultTableModel) jTable1.getModel();
        
        chDate2.addActionDateChooserListener(new DateChooserAdapter() {
            @Override
            public void dateBetweenChanged(DateBetween date, DateChooserAction action) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String dateFrom = df.format(date.getFromDate());
                String toDate = df.format(date.getToDate());
                //loadData("SELECT * FROM `salary_details` WHERE date BETWEEN '" + dateFrom + "' and '" +toDate+"'");
                loadData("SELECT * FROM leavelog WHERE e.empId=s.empId AND fromcutoffdate = '" + dateFrom + "' AND tocutoffdate = '" + toDate + "'");
                //txt_violationdate.setText(null);

            }

        });

        try {
            SingletonConnection.getInstance().connectDatabase();


        } catch (Exception e) {
            System.err.println(e);
        }
        
        chDate2.setSelectedDateBetween(new DateBetween(getLast28Day(), new Date()), true);
        
        notifyContractEndCheck();
        updateAgeWhenBday();
        updateStatusViaLastDay();
        
        /*txt_contractstatus_empupdt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStatusEmpUpdt(txt_contractstatus_empupdt.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStatusEmpUpdt(txt_contractstatus_empupdt.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStatusEmpUpdt(txt_contractstatus_empupdt.getText());
            }
        });*/
        combo_contractstatus_newemp.addActionListener(e -> updateStatusEmpNew(String.valueOf(combo_contractstatus_newemp.getSelectedItem())));
        combo_contractstatus_empupdt.addActionListener(e -> updateStatusEmpUpdt(String.valueOf(combo_contractstatus_empupdt.getSelectedItem())));
        txt_bday_newemp.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String newAge = String.valueOf(calculateAgeViaBirthdate(txt_bday_newemp.getText()));
                txt_age_newemp.setText(newAge);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                
            }
        });
        
        txt_contractstart_empupdt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
               // Given date
                String givenDateStr = txt_contractstart_empupdt.getText(); // Format: yyyy-mm-dd
                LocalDate givenDate = LocalDate.parse(givenDateStr);

                // Calculate 6 months later
                LocalDate sixMonthsLater = givenDate.plusMonths(6);

                // Format the result
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String contractEnd = sixMonthsLater.format(formatter);
                txt_contractend_empupdt.setText(contractEnd);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                
            }
        });

        // Add action listener to "Select All" checkbox
        cbSelectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selectAllChecked = cbSelectAll.isSelected();
                cbActive.setSelected(selectAllChecked);
                cbNLC.setSelected(selectAllChecked);
                cbRendering.setSelected(selectAllChecked);
                cbWithdrawn.setSelected(selectAllChecked);
            }
        });
        
        cbSelectAllStation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selectAllStationsChecked = cbSelectAllStation.isSelected();
                cbManila.setSelected(selectAllStationsChecked);
                cbClark.setSelected(selectAllStationsChecked);
                cbCebu.setSelected(selectAllStationsChecked);
            }
        });
        
        cbSelectAllDepartments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selectAllDepartmentsChecked = cbSelectAllDepartments.isSelected();
                cbBuildingMaintenance.setSelected(selectAllDepartmentsChecked);
                cbCleaning.setSelected(selectAllDepartmentsChecked);
                cbExecutive.setSelected(selectAllDepartmentsChecked);
                cbFinance.setSelected(selectAllDepartmentsChecked);
                cbGroundHandling.setSelected(selectAllDepartmentsChecked);
                cbGSE.setSelected(selectAllDepartmentsChecked);
                cbHR.setSelected(selectAllDepartmentsChecked);
                cbMaintenance.setSelected(selectAllDepartmentsChecked);
                cbOCC.setSelected(selectAllDepartmentsChecked);
                cbOGM.setSelected(selectAllDepartmentsChecked);
                cbOperation.setSelected(selectAllDepartmentsChecked);
                cbPestControl.setSelected(selectAllDepartmentsChecked);
                cbQuality.setSelected(selectAllDepartmentsChecked);
                cbShop.setSelected(selectAllDepartmentsChecked);
                cbTechnicalRecords.setSelected(selectAllDepartmentsChecked);
                cbToolRoom.setSelected(selectAllDepartmentsChecked);
                cbTraining.setSelected(selectAllDepartmentsChecked);
                cbWarehouse.setSelected(selectAllDepartmentsChecked);
            }
        });
        
        cbSelectAllClients.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean selectAllClientsChecked = cbSelectAllClients.isSelected();
        cbPalexCebu.setSelected(selectAllClientsChecked);
        cbPalex.setSelected(selectAllClientsChecked);
        cbDotaCebu.setSelected(selectAllClientsChecked);
        cbDotaClark.setSelected(selectAllClientsChecked);
        cbCebuPacificCebu.setSelected(selectAllClientsChecked);
        cbCebuPacificClark.setSelected(selectAllClientsChecked);
        cbCebuPacific.setSelected(selectAllClientsChecked);
        cbMacroAsia.setSelected(selectAllClientsChecked);
        cbAirAsia.setSelected(selectAllClientsChecked);
        cbDota.setSelected(selectAllClientsChecked);
        cbAirswift.setSelected(selectAllClientsChecked);
    }
});

        
        ActionListener filterListener = e -> {
             StringBuilder queryBuilder = new StringBuilder("SELECT * FROM empinfo WHERE 1=1");

        // Status conditions
        boolean selectAllStatusSelected = cbSelectAll.isSelected();
        boolean activeSelected = cbActive.isSelected();
        boolean nlcSelected = cbNLC.isSelected();
        boolean renderingSelected = cbRendering.isSelected();
        boolean withdrawnSelected = cbWithdrawn.isSelected();

        // Station conditions
        boolean selectAllStationsSelected = cbSelectAllStation.isSelected();
        boolean manilaSelected = cbManila.isSelected();
        boolean clarkSelected = cbClark.isSelected();
        boolean cebuSelected = cbCebu.isSelected();
        
        // Department conditions
        boolean selectAllDepartmentsSelected = cbSelectAllDepartments.isSelected();
        boolean buildingMaintenanceSelected = cbBuildingMaintenance.isSelected();
        boolean cleaningSelected = cbCleaning.isSelected();
        boolean executiveSelected = cbExecutive.isSelected();
        boolean financeSelected = cbFinance.isSelected();
        boolean groundHandlingSelected = cbGroundHandling.isSelected();
        boolean gseSelected = cbGSE.isSelected();
        boolean hrSelected = cbHR.isSelected();
        boolean maintenanceSelected = cbMaintenance.isSelected();
        boolean occSelected = cbOCC.isSelected();
        boolean ogmSelected = cbOGM.isSelected();
        boolean operationSelected = cbOperation.isSelected();
        boolean pestControlSelected = cbPestControl.isSelected();
        boolean qualitySelected = cbQuality.isSelected();
        boolean shopSelected = cbShop.isSelected();
        boolean technicalRecordsSelected = cbTechnicalRecords.isSelected();
        boolean toolRoomSelected = cbToolRoom.isSelected();
        boolean trainingSelected = cbTraining.isSelected();
        boolean warehouseSelected = cbWarehouse.isSelected();
        
        // Client conditions
        boolean selectAllClientsSelected = cbSelectAllClients.isSelected();
        boolean palexCebuSelected = cbPalexCebu.isSelected();
        boolean palexSelected = cbPalex.isSelected();
        boolean dotaCebuSelected = cbDotaCebu.isSelected();
        boolean dotaClarkSelected = cbDotaClark.isSelected();
        boolean cebuPacificCebuSelected = cbCebuPacificCebu.isSelected();
        boolean cebuPacificClarkSelected = cbCebuPacificClark.isSelected();
        boolean cebuPacificSelected = cbCebuPacific.isSelected();
        boolean macroAsiaSelected = cbMacroAsia.isSelected();
        boolean airAsiaSelected = cbAirAsia.isSelected();
        boolean dotaSelected = cbDota.isSelected();
        boolean airswiftSelected = cbAirswift.isSelected();

        // Building status condition
        if (selectAllStatusSelected || activeSelected || nlcSelected || renderingSelected || withdrawnSelected) {
            queryBuilder.append(" AND (");

            boolean firstStatusConditionAdded = false;

            if (selectAllStatusSelected) {
                queryBuilder.append("status = 'Active' OR status = 'No Longer Connected' OR status = 'Rendering' OR status = 'Withdrawn application'");
            } else {
                if (activeSelected) {
                    queryBuilder.append("status = 'Active'");
                    firstStatusConditionAdded = true;
                }
                if (nlcSelected) {
                    if (firstStatusConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("status = 'No Longer Connected'");
                    firstStatusConditionAdded = true;
                }
                if (renderingSelected) {
                    if (firstStatusConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("status = 'Rendering'");
                    firstStatusConditionAdded = true;
                }
                if (withdrawnSelected) {
                    if (firstStatusConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("status = 'Withdrawn application'");
                }
            }

            queryBuilder.append(")");
        }

        // Building station condition
        if (selectAllStationsSelected || manilaSelected || clarkSelected || cebuSelected) {
            queryBuilder.append(" AND (");

            boolean firstStationConditionAdded = false;

            if (selectAllStationsSelected) {
                queryBuilder.append("station = 'Manila' OR station = 'Clark' OR station = 'Cebu'");
            } else {
                if (manilaSelected) {
                    queryBuilder.append("station = 'Manila'");
                    firstStationConditionAdded = true;
                }
                if (clarkSelected) {
                    if (firstStationConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("station = 'Clark'");
                    firstStationConditionAdded = true;
                }
                if (cebuSelected) {
                    if (firstStationConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("station = 'Cebu'");
                }
            }

            queryBuilder.append(")");
        }

        // Building department condition
        if (selectAllDepartmentsSelected || buildingMaintenanceSelected || cleaningSelected || executiveSelected || financeSelected || groundHandlingSelected || gseSelected || hrSelected || maintenanceSelected || occSelected || ogmSelected || operationSelected || pestControlSelected || qualitySelected || shopSelected || technicalRecordsSelected || toolRoomSelected || trainingSelected || warehouseSelected) {
            queryBuilder.append(" AND (");

            boolean firstDepartmentConditionAdded = false;

            if (selectAllDepartmentsSelected) {
                queryBuilder.append("department = 'Building Maintenance Department' OR department = 'Cleaning Department' OR department = 'Executive' OR department = 'Finance Department' OR department = 'Ground Handling Department' OR department = 'GSE Department' OR department = 'Human Resource Department' OR department = 'Maintenance Department' OR department = 'OCC Department' OR department = 'Office of the General Manager' OR department = 'Operation Department' OR department = 'Pest Control Department' OR department = 'Quality Department' OR department = 'Shop Dept.' OR department = 'Technical Records Department' OR department = 'Tool Room Department' OR department = 'Training Department' OR department = 'Warehouse'");
            } else {
                if (buildingMaintenanceSelected) {
                    queryBuilder.append("department = 'Building Maintenance Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (cleaningSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Cleaning Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (executiveSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Executive'");
                    firstDepartmentConditionAdded = true;
                }
                if (financeSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Finance Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (groundHandlingSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Ground Handling Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (gseSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'GSE Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (hrSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Human Resource Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (maintenanceSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Maintenance Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (occSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'OCC Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (ogmSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Office of the General Manager'");
                    firstDepartmentConditionAdded = true;
                }
                if (operationSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Operation Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (pestControlSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Pest Control Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (qualitySelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Quality Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (shopSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Shop Dept.'");
                    firstDepartmentConditionAdded = true;
                }
                if (technicalRecordsSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Technical Records Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (toolRoomSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Tool Room Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (trainingSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Training Department'");
                    firstDepartmentConditionAdded = true;
                }
                if (warehouseSelected) {
                    if (firstDepartmentConditionAdded) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append("department = 'Warehouse'");
                }
            }

            queryBuilder.append(")");
        }
        
        if (selectAllClientsSelected || palexCebuSelected || palexSelected || dotaCebuSelected || dotaClarkSelected || cebuPacificCebuSelected || cebuPacificClarkSelected || cebuPacificSelected || macroAsiaSelected || airAsiaSelected || dotaSelected || airswiftSelected) {
    queryBuilder.append(" AND (");

    boolean firstClientConditionAdded = false;

    if (selectAllClientsSelected) {
        queryBuilder.append("clients = 'Palex (Cebu)' OR clients = 'PALEX' OR clients = 'DOTA (CEBU)' OR clients = 'DOTA (CLARK)' OR clients = 'Cebu Pacific (Cebu)' OR clients = 'Cebu Pacific (Clark)' OR clients = 'Cebu Pacific' OR clients = 'MacroAsia' OR clients = 'AirAsia' OR clients = 'DOTA' OR clients = 'Airswift'");
    } else {
        if (palexCebuSelected) {
            queryBuilder.append("clients = 'Palex (Cebu)'");
            firstClientConditionAdded = true;
        }
        if (palexSelected) {
            if (firstClientConditionAdded) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("clients = 'PALEX'");
            firstClientConditionAdded = true;
        }
        if (dotaCebuSelected) {
            if (firstClientConditionAdded) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("clients = 'DOTA (CEBU)'");
            firstClientConditionAdded = true;
        }
        if (dotaClarkSelected) {
            if (firstClientConditionAdded) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("clients = 'DOTA (CLARK)'");
            firstClientConditionAdded = true;
        }
        if (cebuPacificCebuSelected) {
            if (firstClientConditionAdded) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("clients = 'Cebu Pacific (Cebu)'");
            firstClientConditionAdded = true;
        }
        if (cebuPacificClarkSelected) {
            if (firstClientConditionAdded) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("clients = 'Cebu Pacific (Clark)'");
            firstClientConditionAdded = true;
        }
        if (cebuPacificSelected) {
            if (firstClientConditionAdded) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("clients = 'Cebu Pacific'");
            firstClientConditionAdded = true;
        }
        if (macroAsiaSelected) {
            if (firstClientConditionAdded) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("clients = 'MacroAsia'");
            firstClientConditionAdded = true;
        }
        if (airAsiaSelected) {
            if (firstClientConditionAdded) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("clients = 'AirAsia'");
            firstClientConditionAdded = true;
        }
        if (dotaSelected) {
            if (firstClientConditionAdded) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("clients = 'DOTA'");
            firstClientConditionAdded = true;
        }
        if (airswiftSelected) {
            if (firstClientConditionAdded) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("clients = 'Airswift'");
        }
    }

    queryBuilder.append(")");
}

            String query = queryBuilder.toString();
            searchEmployee = (DefaultTableModel) jTable4.getModel();
            //System.out.println(query);
            loadData(query);
       
        };
        cbActive.addActionListener(filterListener);
        cbNLC.addActionListener(filterListener);
        cbRendering.addActionListener(filterListener);
        cbWithdrawn.addActionListener(filterListener);
        cbSelectAll.addActionListener(filterListener);
        
        cbSelectAllStation.addActionListener(filterListener);
        cbClark.addActionListener(filterListener);
        cbCebu.addActionListener(filterListener);
        cbManila.addActionListener(filterListener);
        
                cbSelectAllDepartments.addActionListener(filterListener);
                cbBuildingMaintenance.addActionListener(filterListener);
                cbCleaning.addActionListener(filterListener);
                cbExecutive.addActionListener(filterListener);
                cbFinance.addActionListener(filterListener);
                cbGroundHandling.addActionListener(filterListener);
                cbGSE.addActionListener(filterListener);
                cbHR.addActionListener(filterListener);
                cbMaintenance.addActionListener(filterListener);
                cbOCC.addActionListener(filterListener);
                cbOGM.addActionListener(filterListener);
                cbOperation.addActionListener(filterListener);
                cbPestControl.addActionListener(filterListener);
                cbQuality.addActionListener(filterListener);
                cbShop.addActionListener(filterListener);
                cbTechnicalRecords.addActionListener(filterListener);
                cbToolRoom.addActionListener(filterListener);
                cbTraining.addActionListener(filterListener);
                cbWarehouse.addActionListener(filterListener);
                
                cbSelectAllClients.addActionListener(filterListener);
                cbPalexCebu.addActionListener(filterListener);
                cbPalex.addActionListener(filterListener);
                cbDotaCebu.addActionListener(filterListener);
                cbDotaClark.addActionListener(filterListener);
                cbCebuPacificCebu.addActionListener(filterListener);
                cbCebuPacificClark.addActionListener(filterListener);
                cbCebuPacific.addActionListener(filterListener);
                cbMacroAsia.addActionListener(filterListener);
                cbAirAsia.addActionListener(filterListener);
                cbDota.addActionListener(filterListener);
                cbAirswift.addActionListener(filterListener);

        
        
        txt_lastdayofwork_empupdt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Check if backspace key is pressed
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    txt_lastdayofwork_empupdt.setText(null); // Set text to null
                }
            }
        });
        
        
        
        
        
        
       
        
        
    }
    private static void updateEntryCountLabel(JLabel label, DefaultTableModel tableModel) {
        int rowCount = tableModel.getRowCount();
        label.setText("Number of entries: " + rowCount);
    }
    private static void updateSelectedCountLabel(JLabel label, JTable table) {
        int selectedRowCount = table.getSelectedRowCount();
        if (selectedRowCount == 0){
            label.setVisible(false);
        }
        else if (selectedRowCount > 0){
            label.setVisible(true);
        }
        label.setText("Number of selected entries: " + selectedRowCount);
    }
    
    private void updateStatusEmpUpdt (String contractStatus){
        switch(contractStatus.toLowerCase()) 
        {
        case "withdrawn applicant":
          txt_status_empupdt.setText("Withdrawn application");
          break;
        case "resigned":
          txt_status_empupdt.setText("No Longer Connected");
          break;
        case "end of contract":
          txt_status_empupdt.setText("No Longer Connected");
          break;
        case "awol":
          txt_status_empupdt.setText("No Longer Connected");
          break;
        
        default:
          txt_status_empupdt.setText("Active");
          break;
        }
    }
    private void updateStatusEmpNew (String contractStatus){
        switch(contractStatus.toLowerCase()) 
        {
        case "withdrawn applicant":
          txt_status_newemp.setText("Withdrawn application");
          break;
        case "resigned":
          txt_status_newemp.setText("No Longer Connected");
          break;
        case "end of contract":
          txt_status_newemp.setText("No Longer Connected");
          break;
        case "awol":
          txt_status_newemp.setText("No Longer Connected");
          break;
        
        default:
          txt_status_newemp.setText("Active");
          break;
        }
    }
    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        JOptionPane.showMessageDialog(this, "Text copied to clipboard!");
    }
    /*private int calculateAgeViaBirthdate(String birthDateString){

        
            // Parse the birthdate string to LocalDate
            LocalDate birthDate = LocalDate.parse(birthDateString);

            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Calculate the period between the birthdate and the current date
            Period period = Period.between(birthDate, currentDate);

            // Get the years from the period
            int age = period.getYears();

            return age;
        
        
    }*/
    private int calculateAgeViaBirthdate(String birthDateString) {
    try {
        // Parse the birthdate string to LocalDate
        LocalDate birthDate = LocalDate.parse(birthDateString);

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the period between the birthdate and the current date
        Period period = Period.between(birthDate, currentDate);

        // Get the years from the period
        int age = period.getYears();

        return age;
    } catch (DateTimeParseException e) {
        // Handle exception: return a specific value for an invalid birthdate
        // For example, you can return -1 to indicate an error or log an error message
        return 0;
    }
}

    private void notifyContractEndCheck(){
        try {
            SQLRun objSQLRun = new SQLRun();
            String sql = "SELECT * FROM empinfo";

            ResultSet rs = objSQLRun.sqlQuery(sql);
            
            contractEndTableModel = (DefaultTableModel) jTable3.getModel();
            contractEndTableModel.setRowCount(0);
            contractExpiredTableModel = (DefaultTableModel) jTable5.getModel();
            contractExpiredTableModel.setRowCount(0);
            while (rs.next()) {

                String empfname = rs.getString("fname");
                String emplname = rs.getString("lname");
                
                String empCombinedName = empfname + " " + emplname;    
                String emppos = rs.getString("position");
                String empdept = rs.getString("department");
                String status = rs.getString("status");
                String statusLowerCase = status.toLowerCase();
                
                String contractEnd = String.valueOf(rs.getDate("contractend"));
                
            try{
                // Parse the target date string to LocalDate
                LocalDate contractEndDate = LocalDate.parse(contractEnd, DateTimeFormatter.ISO_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference between current date and target date in days
                long daysUntilTargetDate = ChronoUnit.DAYS.between(currentDate, contractEndDate);
                
                // Check if it's time to send the notification (one week or less away)
                if (daysUntilTargetDate <= 7 && daysUntilTargetDate >= 1 && "active".matches(statusLowerCase)) {
                     
                     contractEndTableModel.addRow(new Object[]{empCombinedName, emppos, empdept, contractEnd, daysUntilTargetDate});
                } 
                else if (contractEndDate.isBefore(currentDate)&& "active".matches(statusLowerCase)){
                    contractExpiredTableModel.addRow(new Object[]{empCombinedName, emppos, empdept, contractEnd});
                }
            }
            catch (DateTimeParseException e) {
                // Handle exception for invalid date string
                // For example, you can log an error message, skip the iteration, or handle the error in another way
                System.err.println("Invalid date format for contract end date of: " + empCombinedName);
                continue; // Skip to the next record in the loop
            }
                
            } 
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Failed to Retrieve Data! Please Contact Your System Administrator!\n\n" + ex.getMessage(), "ERROR", 0);
            
        }
        
    }
    
    private void sortByMonthsDone(){
        try {
            SQLRun objSQLRun = new SQLRun();
            String sql = "SELECT * FROM empinfo";

            ResultSet rs = objSQLRun.sqlQuery(sql);
            
            fourMonthsTableModel = (DefaultTableModel) jTable6.getModel();
            fourMonthsTableModel.setRowCount(0);
            while (rs.next()) {

                String empfname = rs.getString("fname");
                String emplname = rs.getString("lname");
                
                String empCombinedName = empfname + " " + emplname;    
                String emppos = rs.getString("position");
                String empdept = rs.getString("department");
                
                String contractStart = String.valueOf(rs.getDate("contractstart"));
                String contractEnd = String.valueOf(rs.getDate("contractend"));
            try{  
                // Parse the target date and contract start date strings to LocalDate
                LocalDate contractEndDate = LocalDate.parse(contractEnd, DateTimeFormatter.ISO_DATE);
                LocalDate contractStartDate = LocalDate.parse(contractStart, DateTimeFormatter.ISO_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the total contract duration in months
                long totalContractDuration = ChronoUnit.MONTHS.between(contractStartDate, contractEndDate);

                // Calculate the time the employee has rendered in months
                long renderedMonths = ChronoUnit.MONTHS.between(contractStartDate, currentDate);

                // Check if the employee has already rendered 4 months in their 6-month contract
                boolean hasRendered4Months = renderedMonths >= monthsCompleted && renderedMonths < totalContractDuration;
                
                
                if (hasRendered4Months ) {
                    fourMonthsTableModel.addRow(new Object[]{empCombinedName, emppos, empdept, contractStart, contractEnd});
                }
            }
            catch (DateTimeParseException e) {
                // Handle exception for invalid date string
                // For example, you can log an error message, skip the iteration, or handle the error in another way
                System.err.println("Invalid date format for contract end date of: " + empCombinedName);
                continue; // Skip to the next record in the loop
            }
                
            } 
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Failed to Retrieve Data! Please Contact Your System Administrator!\n\n" + ex.getMessage(), "ERROR", 0);
            
        }
        
    }
    
    private void updateStatusViaLastDay(){
        try {
            String sql = "SELECT * FROM empinfo WHERE lastdayofwork IS NOT NULL AND lastdayofwork != ''";
            PreparedStatement p = SingletonConnection.getInstance().openConnection().prepareStatement(sql);
            ResultSet r = p.executeQuery();
            
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            while (r.next()) {
                int id = r.getInt("id");
                String emplname = r.getString("lname");
                String empfname = r.getString("fname");
                String beforestatus = r.getString("status");
                 
                String lastDayOfWorkStr = String.valueOf(r.getDate("lastdayofwork"));
                
                // Parse the last day of work date
                LocalDate lastDayOfWork = LocalDate.parse(lastDayOfWorkStr, formatter);

                // Check if the last day of work is before the current date
                if (lastDayOfWork.isBefore(currentDate)) {
                    // Update the employee's status
                    try{ 
                        String sqlUpdateStatus = MessageFormat.format("UPDATE `empinfo` SET `status`= ''No Longer Connected'' WHERE id = ''{0}'' ", id);
                        
                        System.out.println("updated status of: " + empfname + " " + emplname +"from " + beforestatus + "to No Longer Connected");
                        logger.info("updated status of: " + empfname + " " + emplname+"from " + beforestatus + "to No Longer Connected");
                        PreparedStatement p1 = SingletonConnection.getInstance().openConnection().prepareStatement(sqlUpdateStatus);
                        p1.executeUpdate();
                        p1.close();
                    }
                    catch (DateTimeParseException e) {
                        // Handle exception for invalid date string
                        // For example, you can log an error message, skip the iteration, or handle the error in another way
                        logger.severe("Invalid date format for last day of work of: " + empfname + " " + emplname);
                        System.err.println("Invalid date format for last day of work of: " + empfname + " " + emplname);
                        continue; // Skip to the next record in the loop
                    }
                }   
            }
            r.close();
            p.close();
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Failed to Retrieve Data! Please Contact Your System Administrator!\n\n" + ex.getMessage(), "ERROR", 0);
             logger.severe("Error! Failed to Retrieve Data! Please Contact Your System Administrator!\n\n" + ex.getMessage());
        }
        
        try {
            String sql = "SELECT * FROM empinfo WHERE resignation IS NOT NULL AND resignation != ''";
            PreparedStatement p = SingletonConnection.getInstance().openConnection().prepareStatement(sql);
            ResultSet r = p.executeQuery();
            
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            while (r.next()) {
                int id = r.getInt("id");
                String emplname = r.getString("lname");
                String empfname = r.getString("fname");
                String beforestatus = r.getString("status");
                try{
                String lastDayOfWorkStr = String.valueOf(r.getDate("lastdayofwork"));
                
                // Parse the last day of work date
                LocalDate lastDayOfWork = LocalDate.parse(lastDayOfWorkStr, formatter);
                
                // Check if the last day of work is after the current date
                if (lastDayOfWork.isAfter(currentDate)) {
                    // Update the employee's status
                    try{ 
                        String sqlUpdateStatus = MessageFormat.format("UPDATE `empinfo` SET `status`= ''Rendering'' WHERE id = ''{0}'' ", id);
                        
                        System.out.println("updated status of: " + empfname + " " + emplname +"from " + beforestatus + "to Rendering");
                        logger.info("updated status of: " + empfname + " " + emplname+"from " + beforestatus + "to Rendering");
                        PreparedStatement p1 = SingletonConnection.getInstance().openConnection().prepareStatement(sqlUpdateStatus);
                        p1.executeUpdate();
                        p1.close();
                    }
                    catch (DateTimeParseException e) {
                        // Handle exception for invalid date string
                        // For example, you can log an error message, skip the iteration, or handle the error in another way
                        System.err.println("Invalid date format for last day of work of: " + empfname + " " + emplname);
                        logger.severe("Invalid date format for last day of work of: " + empfname + " " + emplname);
                        continue; // Skip to the next record in the loop
                    }
                }
                }
                catch (DateTimeParseException e) {
                        // Handle exception for invalid date string
                        // For example, you can log an error message, skip the iteration, or handle the error in another way
                        System.err.println("Invalid date format for last day of work of: " + empfname + " " + emplname);
                        logger.severe("Invalid date format for last day of work of: " + empfname + " " + emplname);
                        continue; // Skip to the next record in the loop
                    }
            }
            r.close();
            p.close();
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Failed to Retrieve Data! Please Contact Your System Administrator!\n\n" + ex.getMessage(), "ERROR", 0);
            logger.severe("Error! Failed to Retrieve Data! Please Contact Your System Administrator!\n\n" + ex.getMessage());
             
        }
    }
    
    private void updateAgeWhenBday(){
        try {
            String sql = "SELECT * FROM empinfo";
            PreparedStatement p = SingletonConnection.getInstance().openConnection().prepareStatement(sql);
            ResultSet r = p.executeQuery();

            while (r.next()) {
                int id = r.getInt("id");
                String empfname = r.getString("fname");
                String emplname = r.getString("lname"); 
                int age = r.getInt("age");
                String bday = String.valueOf(r.getDate("birthday"));
                try{
                int newAge = calculateAgeViaBirthdate(bday);
                    
                String sqlUpdateBirthday = MessageFormat.format("UPDATE `empinfo` SET `age`=''{0}'' WHERE id = ''{1}'' ", newAge, id);
                PreparedStatement p1 = SingletonConnection.getInstance().openConnection().prepareStatement(sqlUpdateBirthday);
                int rowsUpdated = p1.executeUpdate();
                p1.close();
                }
                catch (DateTimeParseException e) {
                    // Handle exception for invalid date string
                    // For example, you can log an error message, skip the iteration, or handle the error in another way
                    System.err.println("Invalid date format for birthdate of: " + empfname + " " + emplname);
                    continue; // Skip to the next record in the loop
                }
              
            }
            r.close();
            p.close();
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Failed to Retrieve Data! Please Contact Your System Administrator!\n\n" + ex.getMessage(), "ERROR", 0);
            
        }
        
    }

    public boolean insertLeavelog() {
        SQLRun objSQLRun = new SQLRun();
        /*String sql = "INSERT INTO employee (empId,nic,fname,lname,dob,address,city,tel_home,tel_mobile,designation,"
                + "department,date_of_joining,gender,salType) VALUES ('" + empId + "','" + nic + "','" + fName + "','" + lName + "','" + dob + "',"
                + "'" + address + "','" + city + "','" + telHome + "','" + telMobile + "','" + designation + "',"
                + "'" + department + "','" + dateOfJoining + "','" + gender + "','" + salType + "')";*/
 /*String sql = "INSERT INTO leavelog (`No`, `Employee_Name`, `Position`, `Date_Filed`, `Type_Of_Loa`, `Inclusive_Date`, `no_of_days`, `Approved_days`, `Approval_By`, `HRD`) "
                + "VALUES ('" + objEmployee.getEmpId() + "','" + objEmployee.getFname() + " " + objEmployee.getLname() + "','"
                + objEmployee.getDesignation() + "','"
                + txtfld_datefiled.getText() +  "', '" + txtfld_loatype.getText() + "', '"+ txtfld_inclusivedate.getText() + "', '"+ txtfld_numofdays.getText() + "', '"+ txtfld_approveddays.getText() + "', '"+ txtfld_approvalby.getText() + "', '"+ txtfld_hrd.getText()+ "')";
        
        int inserted = objSQLRun.sqlUpdate(sql);
        
        if (inserted > 0) {
            JOptionPane.showMessageDialog(null, "Employee " + objEmployee.getFname() + objEmployee.getLname() + " has been added "
                    + "to the system successfully", "Success", 1);
            return true;

        } else {
            JOptionPane.showMessageDialog(null, "Error occurred while trying to add Employee "
                    + "" + objEmployee.getFname() + objEmployee.getLname() + " to the system", "ERROR", 0);
            return false;

        }
         */
        return false;

    }

    private void loadData(String sql) {
    try {
        searchEmployee.setRowCount(0);
        PreparedStatement p = SingletonConnection.getInstance().openConnection().prepareStatement(sql);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            String lname = r.getString("lname");
            String fname = r.getString("fname");
            String mname = r.getString("mname");
            String gender = r.getString("gender");
            String birthday = r.getString("birthday");
            int age = r.getInt("age");
            String philhealth = r.getString("philhealth");
            String pagibig = r.getString("pagibig");
            String sss = r.getString("sss");
            String tin = r.getString("tin");
            String address = r.getString("address");
            String nationality = r.getString("nationality");
            String position = r.getString("position");
            String department = r.getString("department");
            String station = r.getString("station");
            String clients = r.getString("clients");
            String contractstatus = r.getString("contractstatus");
            String resignation = r.getString("resignation");
            String datehired = r.getString("datehired");
            String lastdayofwork = r.getString("lastdayofwork");
            String dateofawol = r.getString("dateofawol");
            String status = r.getString("status");
            String attrition = r.getString("attrition");
            boolean firstContract = r.getBoolean("firstcontract"); // Replace with your checkbox column name
                boolean secondContract = r.getBoolean("secondcontract"); // Replace with your checkbox column name
                boolean thirdContract = r.getBoolean("thirdcontract"); 
            String contractstart = r.getString("contractstart");
            String contractend = r.getString("contractend");
            String awol = r.getString("awol");

            searchEmployee.addRow(new Object[]{lname, fname, mname, gender, birthday, age, philhealth, pagibig, sss, tin, address,
                                       nationality, position, department, station,clients, contractstatus, resignation, datehired,
                                       lastdayofwork, dateofawol, status, attrition, firstContract, secondContract, thirdContract, contractstart, contractend, awol});
        }
        r.close();
        p.close();
    } catch (Exception e) {
        System.err.println(e);
    }
}

    private Date getLast28Day() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -28);
        return cal.getTime();
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public void exportarExcel(JTable jt) {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(jt);
            File saveFile = jFileChooser.getSelectedFile();
            if (saveFile != null) {
                saveFile = new File(saveFile.toString() + ".xlsx");
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("benefits");
                Row rowCol = sheet.createRow(0);

                for (int i = 0; i < jt.getColumnCount(); i++) {
                    Cell cell = rowCol.createCell(i);
                    cell.setCellValue(jt.getColumnName(i));
                }

                for (int j = 0; j < jt.getRowCount(); j++) {
                    Row row = sheet.createRow(j + 1);
                    for (int k = 0; k < jt.getColumnCount(); k++) {
                        Cell cell = row.createCell(k);
                        if (jt.getValueAt(j, k) != null) {
                            cell.setCellValue(jt.getValueAt(j, k).toString());
                        }
                    }
                }
                FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                wb.write(out);
                wb.close();
                out.close();
                openFile(saveFile.toString());

            } else {
                JOptionPane.showMessageDialog(null, "Error generating file");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException io) {
            System.out.println(io);
        }
    }
    
    public void exportCertain(JTable jt) {
    try {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.showSaveDialog(jt);
        File saveFile = jFileChooser.getSelectedFile();
        if (saveFile != null) {
            saveFile = new File(saveFile.toString() + ".xlsx");
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("monitoring");
            Row rowCol = sheet.createRow(0);

            // Columns to export: last name, first name, middle name, position, department
            // Adjust these indices according to your JTable's column order
            int[] columnsToExport = {0, 1, 2, 12, 13, 21, 19}; // Update these indices based on actual column indices

            // Sort the JTable by department before exporting
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(jt.getModel());
            jt.setRowSorter(sorter);
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(13, SortOrder.ASCENDING)); // Sort by department in ascending order
            sorter.setSortKeys(sortKeys);
            sorter.sort();

            // Export column headers
            for (int i = 0; i < columnsToExport.length; i++) {
                Cell cell = rowCol.createCell(i);
                cell.setCellValue(jt.getColumnName(columnsToExport[i]));
            }

            // Export data rows
            int rowIndex = 1;
            for (int j = 0; j < jt.getRowCount(); j++) {
                Row row = sheet.createRow(rowIndex++);
                for (int k = 0; k < columnsToExport.length; k++) {
                    Cell cell = row.createCell(k);
                    Object value = jt.getValueAt(j, columnsToExport[k]);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                }
            }

            // Add status count rows
            Map<String, Integer> statusCounts = new HashMap<>();
            for (int j = 0; j < jt.getRowCount(); j++) {
                Object statusValue = jt.getValueAt(j, 21); // Get the status column value
                if (statusValue != null) {
                    String status = statusValue.toString();
                    statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
                }
            }
            for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
                Row statusRow = sheet.createRow(rowIndex++);
                Cell statusCellLabel = statusRow.createCell(0);
                statusCellLabel.setCellValue("Status Count: " + entry.getKey());
                Cell statusCellValue = statusRow.createCell(1);
                statusCellValue.setCellValue(entry.getValue());
            }

            // Add count of entries at the end
            int totalEntries = jt.getRowCount();
            Row countRow = sheet.createRow(rowIndex++);
            Cell countCellLabel = countRow.createCell(0);
            countCellLabel.setCellValue("Total Entries:");
            Cell countCellValue = countRow.createCell(1);
            countCellValue.setCellValue(totalEntries);

            FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
            wb.write(out);
            wb.close();
            out.close();
            openFile(saveFile.toString());

        } else {
            JOptionPane.showMessageDialog(null, "Error generating file");
        }
    } catch (FileNotFoundException e) {
        System.out.println(e);
    } catch (IOException io) {
        System.out.println(io);
    }
}


    public int findNetPayColumnIndex(JTable jt, String netPayColumnName) {
        int columnIndex = -1; // Default value if column is not found

        // Iterate over column headers to find the net pay column
        for (int i = 0; i < jt.getColumnCount(); i++) {
            String columnName = jt.getColumnName(i);
            if (columnName.equals(netPayColumnName)) {
                columnIndex = i;
                break; // Column found, exit loop
            }
        }

        return columnIndex;
    }

    public int findTotalDeductionsColumnIndex(JTable jt, String totalDeductionsColumnName) {
        int columnIndex = -1; // Default value if column is not found

        // Iterate over column headers to find the total deductions column
        for (int i = 0; i < jt.getColumnCount(); i++) {
            String columnName = jt.getColumnName(i);
            if (columnName.equals(totalDeductionsColumnName)) {
                columnIndex = i;
                break; // Column found, exit loop
            }
        }

        return columnIndex;
    }

    public int findColumnIndexByName(JTable jt, String columnName) {
        int columnIndex = -1; // Default value if column is not found

        // Iterate over column headers to find the column by name
        for (int i = 0; i < jt.getColumnCount(); i++) {
            String columnHeaderText = jt.getColumnName(i);
            if (columnHeaderText.equals(columnName)) {
                columnIndex = i;
                break; // Column found, exit loop
            }
        }

        return columnIndex;
    }

    public double calculateTotalColumnSum(JTable jt, int columnIndex) {
        double totalSum = 0.0;

        // Iterate over rows to calculate the total sum of the column
        for (int j = 0; j < jt.getRowCount(); j++) {
            Object value = jt.getValueAt(j, columnIndex);
            if (value != null && value instanceof Number) {
                totalSum += ((Number) value).doubleValue();
            }
        }

        return totalSum;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup_rd = new javax.swing.ButtonGroup();
        jDialog3 = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        leave_empfname = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        leave_emplname = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jDialog4 = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        employee_emplname1 = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jDialog5 = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        employee_desig = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jDialog6 = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jLabel72 = new javax.swing.JLabel();
        employee_dept = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jDialog7 = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        log_empfname = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        log_emplname = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jDialog8 = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        log_empfname1 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        log_emplname1 = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        jDialog9 = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        del_employee_empfname2 = new javax.swing.JTextField();
        jButton18 = new javax.swing.JButton();
        del_employee_emplname2 = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jDialog10 = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        updt_empfname2 = new javax.swing.JTextField();
        jButton19 = new javax.swing.JButton();
        updt_emplname2 = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        jDialog11 = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        benefitssearch_empfname3 = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        benefitssearch_emplname3 = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jDialog12 = new javax.swing.JDialog();
        jPanel12 = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        station_txtfld = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jDialog13 = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        jLabel83 = new javax.swing.JLabel();
        monthsCompleted_txtfld1 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jDialog14 = new javax.swing.JDialog();
        jPanel14 = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        status_txtfld2 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jDialog15 = new javax.swing.JDialog();
        jPanel15 = new javax.swing.JPanel();
        cbActive = new javax.swing.JCheckBox();
        cbNLC = new javax.swing.JCheckBox();
        cbRendering = new javax.swing.JCheckBox();
        cbWithdrawn = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        cbSelectAll = new javax.swing.JCheckBox();
        jDialog16 = new javax.swing.JDialog();
        jPanel16 = new javax.swing.JPanel();
        cbManila = new javax.swing.JCheckBox();
        cbClark = new javax.swing.JCheckBox();
        cbCebu = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        cbSelectAllStation = new javax.swing.JCheckBox();
        jDialog17 = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel17 = new javax.swing.JPanel();
        cbBuildingMaintenance = new javax.swing.JCheckBox();
        cbCleaning = new javax.swing.JCheckBox();
        cbExecutive = new javax.swing.JCheckBox();
        jLabel19 = new javax.swing.JLabel();
        cbSelectAllDepartments = new javax.swing.JCheckBox();
        cbFinance = new javax.swing.JCheckBox();
        cbGroundHandling = new javax.swing.JCheckBox();
        cbGSE = new javax.swing.JCheckBox();
        cbHR = new javax.swing.JCheckBox();
        cbPestControl = new javax.swing.JCheckBox();
        cbMaintenance = new javax.swing.JCheckBox();
        cbTechnicalRecords = new javax.swing.JCheckBox();
        cbOCC = new javax.swing.JCheckBox();
        cbShop = new javax.swing.JCheckBox();
        cbOperation = new javax.swing.JCheckBox();
        cbToolRoom = new javax.swing.JCheckBox();
        cbOGM = new javax.swing.JCheckBox();
        cbQuality = new javax.swing.JCheckBox();
        cbWarehouse = new javax.swing.JCheckBox();
        cbTraining = new javax.swing.JCheckBox();
        jDialog18 = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel18 = new javax.swing.JPanel();
        cbAirAsia = new javax.swing.JCheckBox();
        cbCebuPacific = new javax.swing.JCheckBox();
        cbCebuPacificCebu = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        cbSelectAllClients = new javax.swing.JCheckBox();
        cbCebuPacificClark = new javax.swing.JCheckBox();
        cbDota = new javax.swing.JCheckBox();
        cbDotaCebu = new javax.swing.JCheckBox();
        cbDotaClark = new javax.swing.JCheckBox();
        cbMacroAsia = new javax.swing.JCheckBox();
        cbPalex = new javax.swing.JCheckBox();
        cbPalexCebu = new javax.swing.JCheckBox();
        cbAirswift = new javax.swing.JCheckBox();
        intFrame_employee_new = new javax.swing.JInternalFrame();
        btn_exit = new javax.swing.JButton();
        btn_add = new javax.swing.JButton();
        panel_empDetails_payroll3 = new javax.swing.JPanel();
        lbl_fname_allowance4 = new javax.swing.JLabel();
        lbl_lname_allowance6 = new javax.swing.JLabel();
        lbl_desig_allowance3 = new javax.swing.JLabel();
        lbl_depart_allowance8 = new javax.swing.JLabel();
        txt_fname_newemp = new javax.swing.JTextField();
        txt_lname_newemp = new javax.swing.JTextField();
        txt_position_newemp = new javax.swing.JTextField();
        txt_depart_newemp = new javax.swing.JTextField();
        lbl_fname_allowance5 = new javax.swing.JLabel();
        txt_mname_newemp = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txt_SSS_newemp = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txt_philhealth_newemp = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txt_pagibig_newemp = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txt_tin_newemp = new javax.swing.JTextField();
        lbl_lname_allowance7 = new javax.swing.JLabel();
        txt_gender_newemp = new javax.swing.JTextField();
        lbl_lname_allowance8 = new javax.swing.JLabel();
        txt_bday_newemp = new javax.swing.JTextField();
        lbl_lname_allowance9 = new javax.swing.JLabel();
        txt_age_newemp = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txt_address_newemp = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txt_nationality_newemp = new javax.swing.JTextField();
        lbl_depart_allowance9 = new javax.swing.JLabel();
        txt_clients_newemp = new javax.swing.JTextField();
        lbl_depart_allowance10 = new javax.swing.JLabel();
        txt_resignation_newemp = new javax.swing.JTextField();
        lbl_depart_allowance11 = new javax.swing.JLabel();
        lbl_depart_allowance12 = new javax.swing.JLabel();
        txt_datehired_newemp = new javax.swing.JTextField();
        lbl_depart_allowance13 = new javax.swing.JLabel();
        txt_lastdayofwork_newemp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_dateofawol_newemp = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_status_newemp = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        combo_attrition_newemp = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txt_station_newemp = new javax.swing.JTextField();
        combo_contractstatus_newemp = new javax.swing.JComboBox<>();
        intFrame_employee_update = new javax.swing.JInternalFrame();
        btn_update = new javax.swing.JButton();
        btn_exit_update = new javax.swing.JButton();
        btn_search_update = new javax.swing.JButton();
        panel_empDetails_payroll4 = new javax.swing.JPanel();
        lbl_fname_allowance6 = new javax.swing.JLabel();
        lbl_lname_allowance10 = new javax.swing.JLabel();
        lbl_desig_allowance4 = new javax.swing.JLabel();
        lbl_depart_allowance14 = new javax.swing.JLabel();
        txt_fname_empupdt = new javax.swing.JTextField();
        txt_lname_empupdt = new javax.swing.JTextField();
        txt_position_empupdt = new javax.swing.JTextField();
        txt_depart_empupdt = new javax.swing.JTextField();
        lbl_fname_allowance7 = new javax.swing.JLabel();
        txt_mname_empupdt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txt_SSS_empupdt = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txt_philhealth_empupdt = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txt_pagibig_empupdt = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txt_tin_empupdt = new javax.swing.JTextField();
        lbl_lname_allowance11 = new javax.swing.JLabel();
        txt_gender_empupdt = new javax.swing.JTextField();
        lbl_lname_allowance12 = new javax.swing.JLabel();
        txt_bday_empupdt = new javax.swing.JTextField();
        lbl_lname_allowance13 = new javax.swing.JLabel();
        txt_age_empupdt = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txt_address_empupdt = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txt_nationality_empupdt = new javax.swing.JTextField();
        lbl_depart_allowance15 = new javax.swing.JLabel();
        txt_clients_empupdt = new javax.swing.JTextField();
        lbl_depart_allowance16 = new javax.swing.JLabel();
        txt_resignation_empupdt = new javax.swing.JTextField();
        lbl_depart_allowance17 = new javax.swing.JLabel();
        lbl_depart_allowance18 = new javax.swing.JLabel();
        txt_datehired_empupdt = new javax.swing.JTextField();
        lbl_depart_allowance19 = new javax.swing.JLabel();
        txt_lastdayofwork_empupdt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_dateofawol_empupdt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_status_empupdt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        combo_attrition_empupdt = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txt_station_empupdt = new javax.swing.JTextField();
        combo_contractstatus_empupdt = new javax.swing.JComboBox<>();
        combo_awol_empupdt = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txt_contractstart_empupdt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txt_contractend_empupdt = new javax.swing.JTextField();
        cbFirstContract = new javax.swing.JCheckBox();
        cbSecondContract = new javax.swing.JCheckBox();
        cbThirdContract = new javax.swing.JCheckBox();
        intFrame_employee_search = new javax.swing.JInternalFrame();
        jScrollPane_tableContainer = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        btn_searchEmp1 = new javax.swing.JButton();
        btn_searchEmp3 = new javax.swing.JButton();
        btn_searchEmp4 = new javax.swing.JButton();
        lblTableCount = new javax.swing.JLabel();
        lblSelectedCount = new javax.swing.JLabel();
        btn_searchEmp6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btn_searchEmp7 = new javax.swing.JButton();
        btn_searchEmp5 = new javax.swing.JButton();
        intFrame_fourmonths = new javax.swing.JInternalFrame();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        intFrame_endofcontract = new javax.swing.JInternalFrame();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        lbl_pms = new javax.swing.JLabel();
        lbl_background = new javax.swing.JLabel();
        menu_menuBar = new javax.swing.JMenuBar();
        menuBar_file = new javax.swing.JMenu();
        menuBar_file_logout = new javax.swing.JMenuItem();
        menuBar_file_exit = new javax.swing.JMenuItem();
        menuBar_employee = new javax.swing.JMenu();
        menuBar_employee_new = new javax.swing.JMenuItem();
        menuBar_employee_update = new javax.swing.JMenuItem();
        menuBar_employee_delete = new javax.swing.JMenuItem();
        menuBar_employee_search = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        jDialog3.setLocation(new java.awt.Point(500, 250));
        jDialog3.setResizable(false);
        jDialog3.setSize(new java.awt.Dimension(500, 250));

        jLabel62.setText("Employee First Name:");

        leave_empfname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leave_empfnameActionPerformed(evt);
            }
        });

        jButton6.setText("Search");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        leave_emplname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leave_emplnameActionPerformed(evt);
            }
        });

        jLabel64.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62)
                            .addComponent(jLabel64))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(leave_empfname, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(leave_emplname, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(leave_empfname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(leave_emplname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton6)
                .addContainerGap(263, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog4.setLocation(new java.awt.Point(500, 250));
        jDialog4.setResizable(false);
        jDialog4.setSize(new java.awt.Dimension(500, 250));

        jButton7.setText("Search");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        employee_emplname1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_emplname1ActionPerformed(evt);
            }
        });

        jLabel70.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel70)
                        .addGap(7, 7, 7)
                        .addComponent(employee_emplname1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(employee_emplname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addContainerGap(266, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog4Layout = new javax.swing.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog5.setLocation(new java.awt.Point(500, 250));
        jDialog5.setResizable(false);
        jDialog5.setSize(new java.awt.Dimension(500, 250));

        jLabel71.setText("Employee Designation:");

        employee_desig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_desigActionPerformed(evt);
            }
        });

        jButton8.setText("Search");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel71)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(employee_desig, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel71)
                    .addComponent(employee_desig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8)
                .addContainerGap(303, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog5Layout = new javax.swing.GroupLayout(jDialog5.getContentPane());
        jDialog5.getContentPane().setLayout(jDialog5Layout);
        jDialog5Layout.setHorizontalGroup(
            jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog5Layout.setVerticalGroup(
            jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog6.setLocation(new java.awt.Point(500, 250));
        jDialog6.setResizable(false);
        jDialog6.setSize(new java.awt.Dimension(500, 250));

        jLabel72.setText("Employee Department:");

        employee_dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_deptActionPerformed(evt);
            }
        });

        jButton9.setText("Search");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel72)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(employee_dept, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel72)
                    .addComponent(employee_dept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addContainerGap(303, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog6Layout = new javax.swing.GroupLayout(jDialog6.getContentPane());
        jDialog6.getContentPane().setLayout(jDialog6Layout);
        jDialog6Layout.setHorizontalGroup(
            jDialog6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog6Layout.setVerticalGroup(
            jDialog6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog7.setLocation(new java.awt.Point(500, 250));
        jDialog7.setResizable(false);
        jDialog7.setSize(new java.awt.Dimension(500, 250));

        jLabel73.setText("Employee First Name:");

        log_empfname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                log_empfnameActionPerformed(evt);
            }
        });

        jButton12.setText("Search");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        log_emplname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                log_emplnameActionPerformed(evt);
            }
        });

        jLabel74.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton12, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel73)
                        .addGap(18, 18, 18)
                        .addComponent(log_empfname, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel74)
                        .addGap(18, 18, 18)
                        .addComponent(log_emplname, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel73)
                    .addComponent(log_empfname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel74)
                    .addComponent(log_emplname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jButton12)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog7Layout = new javax.swing.GroupLayout(jDialog7.getContentPane());
        jDialog7.getContentPane().setLayout(jDialog7Layout);
        jDialog7Layout.setHorizontalGroup(
            jDialog7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog7Layout.setVerticalGroup(
            jDialog7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialog8.setLocation(new java.awt.Point(500, 250));
        jDialog8.setResizable(false);
        jDialog8.setSize(new java.awt.Dimension(500, 250));

        jLabel75.setText("Employee First Name:");

        log_empfname1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                log_empfname1ActionPerformed(evt);
            }
        });

        jButton17.setText("Search");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        log_emplname1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                log_emplname1ActionPerformed(evt);
            }
        });

        jLabel76.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton17, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel75)
                        .addGap(18, 18, 18)
                        .addComponent(log_empfname1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel76)
                        .addGap(18, 18, 18)
                        .addComponent(log_emplname1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel75)
                    .addComponent(log_empfname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel76)
                    .addComponent(log_emplname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jButton17)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog8Layout = new javax.swing.GroupLayout(jDialog8.getContentPane());
        jDialog8.getContentPane().setLayout(jDialog8Layout);
        jDialog8Layout.setHorizontalGroup(
            jDialog8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog8Layout.setVerticalGroup(
            jDialog8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialog9.setLocation(new java.awt.Point(500, 250));
        jDialog9.setResizable(false);
        jDialog9.setSize(new java.awt.Dimension(500, 250));

        jLabel67.setText("Employee First Name:");

        del_employee_empfname2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                del_employee_empfname2ActionPerformed(evt);
            }
        });

        jButton18.setText("Delete");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        del_employee_emplname2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                del_employee_emplname2ActionPerformed(evt);
            }
        });

        jLabel77.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton18)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel67)
                            .addComponent(jLabel77))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(del_employee_empfname2, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(del_employee_emplname2, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(del_employee_empfname2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel77)
                    .addComponent(del_employee_emplname2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton18)
                .addContainerGap(263, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog9Layout = new javax.swing.GroupLayout(jDialog9.getContentPane());
        jDialog9.getContentPane().setLayout(jDialog9Layout);
        jDialog9Layout.setHorizontalGroup(
            jDialog9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog9Layout.setVerticalGroup(
            jDialog9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog10.setLocation(new java.awt.Point(500, 250));
        jDialog10.setResizable(false);
        jDialog10.setSize(new java.awt.Dimension(500, 250));

        jLabel78.setText("Employee First Name:");

        updt_empfname2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updt_empfname2ActionPerformed(evt);
            }
        });

        jButton19.setText("Search");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        updt_emplname2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updt_emplname2ActionPerformed(evt);
            }
        });

        jLabel79.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton19, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel78)
                        .addGap(18, 18, 18)
                        .addComponent(updt_empfname2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel79)
                        .addGap(18, 18, 18)
                        .addComponent(updt_emplname2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(updt_empfname2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel79)
                    .addComponent(updt_emplname2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jButton19)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog10Layout = new javax.swing.GroupLayout(jDialog10.getContentPane());
        jDialog10.getContentPane().setLayout(jDialog10Layout);
        jDialog10Layout.setHorizontalGroup(
            jDialog10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog10Layout.setVerticalGroup(
            jDialog10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialog11.setLocation(new java.awt.Point(500, 250));
        jDialog11.setResizable(false);
        jDialog11.setSize(new java.awt.Dimension(500, 250));

        jLabel80.setText("Employee First Name:");

        benefitssearch_empfname3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                benefitssearch_empfname3ActionPerformed(evt);
            }
        });

        jButton21.setText("Search");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        benefitssearch_emplname3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                benefitssearch_emplname3ActionPerformed(evt);
            }
        });

        jLabel81.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton21, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel80)
                        .addGap(18, 18, 18)
                        .addComponent(benefitssearch_empfname3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel81)
                        .addGap(18, 18, 18)
                        .addComponent(benefitssearch_emplname3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(benefitssearch_empfname3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel81)
                    .addComponent(benefitssearch_emplname3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jButton21)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog11Layout = new javax.swing.GroupLayout(jDialog11.getContentPane());
        jDialog11.getContentPane().setLayout(jDialog11Layout);
        jDialog11Layout.setHorizontalGroup(
            jDialog11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog11Layout.setVerticalGroup(
            jDialog11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialog12.setLocation(new java.awt.Point(500, 250));
        jDialog12.setResizable(false);
        jDialog12.setSize(new java.awt.Dimension(500, 250));

        jLabel82.setText("Station:");

        station_txtfld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                station_txtfldActionPerformed(evt);
            }
        });

        jButton10.setText("Search");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel82)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(station_txtfld, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(144, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(station_txtfld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton10)
                .addContainerGap(303, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog12Layout = new javax.swing.GroupLayout(jDialog12.getContentPane());
        jDialog12.getContentPane().setLayout(jDialog12Layout);
        jDialog12Layout.setHorizontalGroup(
            jDialog12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog12Layout.setVerticalGroup(
            jDialog12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog13.setLocation(new java.awt.Point(500, 250));
        jDialog13.setResizable(false);
        jDialog13.setSize(new java.awt.Dimension(500, 250));

        jLabel83.setText("Enter number of months:");

        monthsCompleted_txtfld1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthsCompleted_txtfld1ActionPerformed(evt);
            }
        });

        jButton11.setText("Submit");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton11)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel83)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monthsCompleted_txtfld1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(monthsCompleted_txtfld1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton11)
                .addContainerGap(303, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog13Layout = new javax.swing.GroupLayout(jDialog13.getContentPane());
        jDialog13.getContentPane().setLayout(jDialog13Layout);
        jDialog13Layout.setHorizontalGroup(
            jDialog13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog13Layout.setVerticalGroup(
            jDialog13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog14.setLocation(new java.awt.Point(500, 250));
        jDialog14.setResizable(false);
        jDialog14.setSize(new java.awt.Dimension(500, 250));

        jLabel84.setText("Status:");

        status_txtfld2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                status_txtfld2ActionPerformed(evt);
            }
        });

        jButton13.setText("Submit");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton13)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel84)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(status_txtfld2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(149, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(status_txtfld2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton13)
                .addContainerGap(303, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog14Layout = new javax.swing.GroupLayout(jDialog14.getContentPane());
        jDialog14.getContentPane().setLayout(jDialog14Layout);
        jDialog14Layout.setHorizontalGroup(
            jDialog14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog14Layout.setVerticalGroup(
            jDialog14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog15.setLocation(new java.awt.Point(500, 250));
        jDialog15.setResizable(false);
        jDialog15.setSize(new java.awt.Dimension(500, 250));

        cbActive.setText("Active");
        cbActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbActiveActionPerformed(evt);
            }
        });

        cbNLC.setText("No Longer Connected");

        cbRendering.setText("Rendering");
        cbRendering.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRenderingActionPerformed(evt);
            }
        });

        cbWithdrawn.setText("Withdrawn Application");

        jLabel12.setText("Sort by Status");

        cbSelectAll.setText("Select All");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(cbSelectAll)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbNLC)
                            .addComponent(cbActive)
                            .addComponent(cbRendering)
                            .addComponent(cbWithdrawn)
                            .addComponent(jLabel12))
                        .addGap(14, 249, Short.MAX_VALUE))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbSelectAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbActive)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbNLC)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbRendering)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbWithdrawn))
        );

        javax.swing.GroupLayout jDialog15Layout = new javax.swing.GroupLayout(jDialog15.getContentPane());
        jDialog15.getContentPane().setLayout(jDialog15Layout);
        jDialog15Layout.setHorizontalGroup(
            jDialog15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog15Layout.setVerticalGroup(
            jDialog15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog15Layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 33, Short.MAX_VALUE))
        );

        jDialog16.setLocation(new java.awt.Point(500, 250));
        jDialog16.setResizable(false);
        jDialog16.setSize(new java.awt.Dimension(500, 250));

        cbManila.setText("Manila");
        cbManila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbManilaActionPerformed(evt);
            }
        });

        cbClark.setText("Clark");

        cbCebu.setText("Cebu");
        cbCebu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCebuActionPerformed(evt);
            }
        });

        jLabel13.setText("Sort by Station");

        cbSelectAllStation.setText("Select All");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(cbSelectAllStation)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbClark)
                            .addComponent(cbManila)
                            .addComponent(cbCebu)
                            .addComponent(jLabel13))
                        .addGap(19, 317, Short.MAX_VALUE))))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbSelectAllStation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbManila)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbClark)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCebu)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout jDialog16Layout = new javax.swing.GroupLayout(jDialog16.getContentPane());
        jDialog16.getContentPane().setLayout(jDialog16Layout);
        jDialog16Layout.setHorizontalGroup(
            jDialog16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog16Layout.setVerticalGroup(
            jDialog16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog16Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 33, Short.MAX_VALUE))
        );

        jDialog17.setLocation(new java.awt.Point(500, 250));
        jDialog17.setResizable(false);
        jDialog17.setSize(new java.awt.Dimension(500, 250));

        cbBuildingMaintenance.setText("Building Maintenance Department");
        cbBuildingMaintenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBuildingMaintenanceActionPerformed(evt);
            }
        });

        cbCleaning.setText("Cleaning Department");

        cbExecutive.setText("Executive");
        cbExecutive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbExecutiveActionPerformed(evt);
            }
        });

        jLabel19.setText("Sort by Department");

        cbSelectAllDepartments.setText("Select All");

        cbFinance.setText("Finance Department");
        cbFinance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFinanceActionPerformed(evt);
            }
        });

        cbGroundHandling.setText("Ground Handling Department");
        cbGroundHandling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbGroundHandlingActionPerformed(evt);
            }
        });

        cbGSE.setText("GSE Department");
        cbGSE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbGSEActionPerformed(evt);
            }
        });

        cbHR.setText("Human Resource Department");
        cbHR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbHRActionPerformed(evt);
            }
        });

        cbPestControl.setText("Pest Control Department");
        cbPestControl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPestControlActionPerformed(evt);
            }
        });

        cbMaintenance.setText("Maintenance Department");
        cbMaintenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMaintenanceActionPerformed(evt);
            }
        });

        cbTechnicalRecords.setText("Technical Records Department");
        cbTechnicalRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTechnicalRecordsActionPerformed(evt);
            }
        });

        cbOCC.setText("OCC Department");
        cbOCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOCCActionPerformed(evt);
            }
        });

        cbShop.setText("Shop Dept.");
        cbShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbShopActionPerformed(evt);
            }
        });

        cbOperation.setText("Operation Department");
        cbOperation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOperationActionPerformed(evt);
            }
        });

        cbToolRoom.setText("Tool Room Department");
        cbToolRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbToolRoomActionPerformed(evt);
            }
        });

        cbOGM.setText("Office of the General Manager");
        cbOGM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOGMActionPerformed(evt);
            }
        });

        cbQuality.setText("Quality Department");
        cbQuality.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbQualityActionPerformed(evt);
            }
        });

        cbWarehouse.setText("Warehouse");
        cbWarehouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbWarehouseActionPerformed(evt);
            }
        });

        cbTraining.setText("Training Department");
        cbTraining.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTrainingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbOperation)
                            .addComponent(cbOGM)
                            .addComponent(cbOCC)
                            .addComponent(cbHR)
                            .addComponent(cbGroundHandling)
                            .addComponent(cbGSE)
                            .addComponent(cbCleaning)
                            .addComponent(cbBuildingMaintenance)
                            .addComponent(cbExecutive)
                            .addComponent(jLabel19)
                            .addComponent(cbFinance)
                            .addComponent(cbMaintenance))
                        .addContainerGap(243, Short.MAX_VALUE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSelectAllDepartments)
                            .addComponent(cbPestControl)
                            .addComponent(cbQuality)
                            .addComponent(cbShop)
                            .addComponent(cbTechnicalRecords)
                            .addComponent(cbToolRoom)
                            .addComponent(cbTraining)
                            .addComponent(cbWarehouse))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbSelectAllDepartments)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbBuildingMaintenance)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCleaning)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbExecutive)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbFinance)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbGroundHandling)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbGSE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbHR)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbMaintenance)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOCC)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOGM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOperation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbPestControl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbQuality)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbShop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTechnicalRecords)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbToolRoom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTraining)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbWarehouse)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel17);

        javax.swing.GroupLayout jDialog17Layout = new javax.swing.GroupLayout(jDialog17.getContentPane());
        jDialog17.getContentPane().setLayout(jDialog17Layout);
        jDialog17Layout.setHorizontalGroup(
            jDialog17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jDialog17Layout.setVerticalGroup(
            jDialog17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog17Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDialog18.setLocation(new java.awt.Point(500, 250));
        jDialog18.setResizable(false);
        jDialog18.setSize(new java.awt.Dimension(500, 250));

        cbAirAsia.setText("AirAsia");
        cbAirAsia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAirAsiaActionPerformed(evt);
            }
        });

        cbCebuPacific.setText("Cebu Pacific");

        cbCebuPacificCebu.setText("Cebu Pacific (Cebu)");
        cbCebuPacificCebu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCebuPacificCebuActionPerformed(evt);
            }
        });

        jLabel20.setText("Sort by Department");

        cbSelectAllClients.setText("Select All");

        cbCebuPacificClark.setText("Cebu Pacific (Clark)");
        cbCebuPacificClark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCebuPacificClarkActionPerformed(evt);
            }
        });

        cbDota.setText("DOTA");
        cbDota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDotaActionPerformed(evt);
            }
        });

        cbDotaCebu.setText("DOTA (Cebu)");
        cbDotaCebu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDotaCebuActionPerformed(evt);
            }
        });

        cbDotaClark.setText("DOTA (Clark)");
        cbDotaClark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDotaClarkActionPerformed(evt);
            }
        });

        cbMacroAsia.setText("MacroAsia");
        cbMacroAsia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMacroAsiaActionPerformed(evt);
            }
        });

        cbPalex.setText("PALEX");
        cbPalex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPalexActionPerformed(evt);
            }
        });

        cbPalexCebu.setText("Palex (Cebu)");
        cbPalexCebu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPalexCebuActionPerformed(evt);
            }
        });

        cbAirswift.setText("AirSwift");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbPalexCebu)
                            .addComponent(cbPalex)
                            .addComponent(cbDotaClark)
                            .addComponent(cbDota)
                            .addComponent(cbDotaCebu)
                            .addComponent(cbCebuPacific)
                            .addComponent(cbAirAsia)
                            .addComponent(cbCebuPacificCebu)
                            .addComponent(jLabel20)
                            .addComponent(cbCebuPacificClark)
                            .addComponent(cbMacroAsia))
                        .addContainerGap(264, Short.MAX_VALUE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSelectAllClients)
                            .addComponent(cbAirswift))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbSelectAllClients)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbAirAsia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCebuPacific)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCebuPacificCebu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCebuPacificClark)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbDota)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbDotaCebu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbDotaClark)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbMacroAsia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbPalex)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbPalexCebu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbAirswift)
                .addContainerGap(247, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel18);

        javax.swing.GroupLayout jDialog18Layout = new javax.swing.GroupLayout(jDialog18.getContentPane());
        jDialog18.getContentPane().setLayout(jDialog18Layout);
        jDialog18Layout.setHorizontalGroup(
            jDialog18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jDialog18Layout.setVerticalGroup(
            jDialog18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog18Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Monitoring System | Home");
        setBackground(new java.awt.Color(255, 255, 255));
        setLocation(new java.awt.Point(100, 0));
        setMinimumSize(new java.awt.Dimension(1200, 700));
        setName("Home"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(null);

        intFrame_employee_new.setClosable(true);
        intFrame_employee_new.setTitle("Enter Employee Details");
        intFrame_employee_new.setToolTipText("");
        intFrame_employee_new.setMaximumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_new.setMinimumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_new.setPreferredSize(new java.awt.Dimension(800, 500));
        intFrame_employee_new.setVisible(false);
        intFrame_employee_new.getContentPane().setLayout(null);

        btn_exit.setText("Exit");
        btn_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exitActionPerformed(evt);
            }
        });
        intFrame_employee_new.getContentPane().add(btn_exit);
        btn_exit.setBounds(210, 490, 120, 23);

        btn_add.setText("Add Employee");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });
        intFrame_employee_new.getContentPane().add(btn_add);
        btn_add.setBounds(80, 490, 120, 20);

        panel_empDetails_payroll3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employee Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18), new java.awt.Color(255, 0, 0))); // NOI18N

        lbl_fname_allowance4.setText("First Name");

        lbl_lname_allowance6.setText("Last Name");

        lbl_desig_allowance3.setText("Position");

        lbl_depart_allowance8.setText("Department");

        txt_lname_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_lname_newempActionPerformed(evt);
            }
        });

        lbl_fname_allowance5.setText("Middle Name");

        jLabel14.setText("SSS:");

        txt_SSS_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_SSS_newempActionPerformed(evt);
            }
        });

        jLabel27.setText("PHILHEALTH:");

        txt_philhealth_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_philhealth_newempActionPerformed(evt);
            }
        });

        jLabel28.setText("PAG-IBIG:");

        txt_pagibig_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_pagibig_newempActionPerformed(evt);
            }
        });

        jLabel29.setText("TIN:");

        txt_tin_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tin_newempActionPerformed(evt);
            }
        });

        lbl_lname_allowance7.setText("Gender");

        txt_gender_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_gender_newempActionPerformed(evt);
            }
        });

        lbl_lname_allowance8.setText("Birthday");

        txt_bday_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_bday_newempActionPerformed(evt);
            }
        });

        lbl_lname_allowance9.setText("Age");

        txt_age_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_age_newempActionPerformed(evt);
            }
        });

        jLabel30.setText("Address");

        txt_address_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_address_newempActionPerformed(evt);
            }
        });

        jLabel31.setText("Nationality");

        txt_nationality_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nationality_newempActionPerformed(evt);
            }
        });

        lbl_depart_allowance9.setText("Clients");

        lbl_depart_allowance10.setText("Contract Status");

        lbl_depart_allowance11.setText("Resignation");

        lbl_depart_allowance12.setText("Last Day of Work");

        txt_datehired_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_datehired_newempActionPerformed(evt);
            }
        });

        lbl_depart_allowance13.setText("Date Hired");

        jLabel4.setText("Date of AWOL");

        jLabel5.setText("Status");

        jLabel6.setText("Attrition");

        combo_attrition_newemp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Yes", "No" }));

        jLabel1.setText("Station");

        txt_station_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_station_newempActionPerformed(evt);
            }
        });

        combo_contractstatus_newemp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Transferred", "AWOL", "End of Contract", "Withdrawn applicant", "For Deployment", "Project Employee", "Apprentice", "Probationary", "Resigned", "Training", "Regular" }));
        combo_contractstatus_newemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_contractstatus_newempActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_empDetails_payroll3Layout = new javax.swing.GroupLayout(panel_empDetails_payroll3);
        panel_empDetails_payroll3.setLayout(panel_empDetails_payroll3Layout);
        panel_empDetails_payroll3Layout.setHorizontalGroup(
            panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(lbl_fname_allowance4))
                    .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_fname_allowance5)
                                    .addComponent(lbl_lname_allowance6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_fname_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_mname_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_lname_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(txt_age_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(32, 32, 32)
                                .addComponent(txt_pagibig_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addGap(63, 63, 63)
                                .addComponent(txt_tin_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addGap(26, 26, 26)
                                .addComponent(txt_nationality_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_lname_allowance7)
                                    .addComponent(lbl_lname_allowance8)
                                    .addComponent(lbl_lname_allowance9))
                                .addGap(40, 40, 40)
                                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_bday_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_gender_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_philhealth_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addGap(42, 42, 42)
                                .addComponent(txt_address_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(63, 63, 63)
                                .addComponent(txt_SSS_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                    .addComponent(lbl_depart_allowance12)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_lastdayofwork_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_empDetails_payroll3Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_dateofawol_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_status_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_empDetails_payroll3Layout.createSequentialGroup()
                                            .addComponent(lbl_desig_allowance3)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txt_position_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                            .addComponent(lbl_depart_allowance8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                                            .addComponent(txt_depart_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_empDetails_payroll3Layout.createSequentialGroup()
                                            .addComponent(lbl_depart_allowance11)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txt_resignation_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                            .addComponent(lbl_depart_allowance13, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(31, 31, 31)
                                            .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txt_datehired_newemp, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                                .addComponent(combo_attrition_newemp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                    .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                            .addComponent(lbl_depart_allowance10, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(combo_contractstatus_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                                            .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lbl_depart_allowance9, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel1))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txt_clients_newemp, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                                .addComponent(txt_station_newemp)))))))))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        panel_empDetails_payroll3Layout.setVerticalGroup(
            panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_fname_allowance4)
                    .addComponent(txt_fname_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_desig_allowance3)
                    .addComponent(txt_position_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_fname_allowance5)
                    .addComponent(txt_mname_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_depart_allowance8)
                    .addComponent(txt_depart_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_empDetails_payroll3Layout.createSequentialGroup()
                        .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_lname_allowance6)
                            .addComponent(txt_lname_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_lname_allowance7)
                            .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_gender_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_depart_allowance9)))
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_empDetails_payroll3Layout.createSequentialGroup()
                        .addComponent(txt_station_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_clients_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_bday_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_lname_allowance8)
                    .addComponent(lbl_depart_allowance10)
                    .addComponent(combo_contractstatus_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_age_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_lname_allowance9)
                    .addComponent(lbl_depart_allowance11)
                    .addComponent(txt_resignation_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txt_philhealth_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_depart_allowance13)
                    .addComponent(txt_datehired_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txt_pagibig_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_depart_allowance12)
                    .addComponent(txt_lastdayofwork_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txt_SSS_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txt_dateofawol_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txt_tin_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txt_status_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_address_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel6)
                    .addComponent(combo_attrition_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nationality_newemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        intFrame_employee_new.getContentPane().add(panel_empDetails_payroll3);
        panel_empDetails_payroll3.setBounds(0, 0, 718, 480);

        getContentPane().add(intFrame_employee_new);
        intFrame_employee_new.setBounds(0, 0, 1200, 680);
        try {
            intFrame_employee_new.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        intFrame_employee_update.setClosable(true);
        intFrame_employee_update.setTitle("Update Employee Details");
        intFrame_employee_update.setMaximumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_update.setMinimumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_update.setPreferredSize(new java.awt.Dimension(800, 500));
        intFrame_employee_update.setVisible(false);
        intFrame_employee_update.getContentPane().setLayout(null);

        btn_update.setText("Update Employee");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        intFrame_employee_update.getContentPane().add(btn_update);
        btn_update.setBounds(200, 510, 150, 23);

        btn_exit_update.setText("Exit");
        btn_exit_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exit_updateActionPerformed(evt);
            }
        });
        intFrame_employee_update.getContentPane().add(btn_exit_update);
        btn_exit_update.setBounds(360, 510, 150, 23);

        btn_search_update.setText("Search");
        btn_search_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_search_updateActionPerformed(evt);
            }
        });
        intFrame_employee_update.getContentPane().add(btn_search_update);
        btn_search_update.setBounds(40, 510, 150, 23);

        panel_empDetails_payroll4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employee Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18), new java.awt.Color(255, 0, 0))); // NOI18N

        lbl_fname_allowance6.setText("First Name");

        lbl_lname_allowance10.setText("Last Name");

        lbl_desig_allowance4.setText("Position");

        lbl_depart_allowance14.setText("Department");

        txt_lname_empupdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_lname_empupdtActionPerformed(evt);
            }
        });

        lbl_fname_allowance7.setText("Middle Name");

        jLabel15.setText("SSS:");

        txt_SSS_empupdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_SSS_empupdtActionPerformed(evt);
            }
        });

        jLabel32.setText("PHILHEALTH:");

        txt_philhealth_empupdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_philhealth_empupdtActionPerformed(evt);
            }
        });

        jLabel33.setText("PAG-IBIG:");

        txt_pagibig_empupdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_pagibig_empupdtActionPerformed(evt);
            }
        });

        jLabel34.setText("TIN:");

        txt_tin_empupdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tin_empupdtActionPerformed(evt);
            }
        });

        lbl_lname_allowance11.setText("Gender");

        txt_gender_empupdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_gender_empupdtActionPerformed(evt);
            }
        });

        lbl_lname_allowance12.setText("Birthday");

        txt_bday_empupdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_bday_empupdtActionPerformed(evt);
            }
        });

        lbl_lname_allowance13.setText("Age");

        txt_age_empupdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_age_empupdtActionPerformed(evt);
            }
        });

        jLabel35.setText("Address");

        txt_address_empupdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_address_empupdtActionPerformed(evt);
            }
        });

        jLabel36.setText("Nationality");

        txt_nationality_empupdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nationality_empupdtActionPerformed(evt);
            }
        });

        lbl_depart_allowance15.setText("Clients");

        lbl_depart_allowance16.setText("Contract Status");

        lbl_depart_allowance17.setText("Resignation");

        lbl_depart_allowance18.setText("Last Day of Work");

        txt_datehired_empupdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_datehired_empupdtActionPerformed(evt);
            }
        });

        lbl_depart_allowance19.setText("Date Hired");

        jLabel7.setText("Date of AWOL");

        jLabel8.setText("Status");

        jLabel9.setText("Attrition");

        combo_attrition_empupdt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Yes", "No" }));

        jLabel2.setText("Station");

        combo_contractstatus_empupdt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Transferred", "AWOL", "End of Contract", "Withdrawn applicant", "For Deployment", "Project Employee", "Apprentice", "Probationary", "Resigned", "Training", "Regular" }));

        combo_awol_empupdt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "AWOL" }));

        javax.swing.GroupLayout panel_empDetails_payroll4Layout = new javax.swing.GroupLayout(panel_empDetails_payroll4);
        panel_empDetails_payroll4.setLayout(panel_empDetails_payroll4Layout);
        panel_empDetails_payroll4Layout.setHorizontalGroup(
            panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(lbl_fname_allowance6))
                    .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_fname_allowance7)
                                    .addComponent(lbl_lname_allowance10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_fname_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_mname_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_lname_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(txt_age_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                .addComponent(jLabel33)
                                .addGap(32, 32, 32)
                                .addComponent(txt_pagibig_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addGap(63, 63, 63)
                                .addComponent(txt_tin_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                .addComponent(jLabel36)
                                .addGap(26, 26, 26)
                                .addComponent(txt_nationality_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_lname_allowance11)
                                    .addComponent(lbl_lname_allowance12)
                                    .addComponent(lbl_lname_allowance13))
                                .addGap(40, 40, 40)
                                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_bday_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_gender_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_philhealth_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addGap(42, 42, 42)
                                .addComponent(txt_address_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(63, 63, 63)
                                .addComponent(txt_SSS_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_empDetails_payroll4Layout.createSequentialGroup()
                                        .addComponent(lbl_desig_allowance4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txt_position_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                        .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_depart_allowance19, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel9)
                                            .addComponent(lbl_depart_allowance18)
                                            .addComponent(lbl_depart_allowance17))
                                        .addGap(31, 31, 31)
                                        .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txt_datehired_empupdt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                            .addComponent(txt_lastdayofwork_empupdt, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_dateofawol_empupdt, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_status_empupdt, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(combo_attrition_empupdt, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txt_resignation_empupdt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)))
                                    .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                                        .addComponent(lbl_depart_allowance14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                                        .addComponent(txt_depart_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(combo_awol_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2)
                            .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txt_station_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_empDetails_payroll4Layout.createSequentialGroup()
                                    .addComponent(lbl_depart_allowance15, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(55, 55, 55)
                                    .addComponent(txt_clients_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_empDetails_payroll4Layout.createSequentialGroup()
                                    .addComponent(lbl_depart_allowance16, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(combo_contractstatus_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_empDetails_payroll4Layout.setVerticalGroup(
            panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empDetails_payroll4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_fname_allowance6)
                    .addComponent(txt_fname_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_desig_allowance4)
                    .addComponent(txt_position_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_fname_allowance7)
                    .addComponent(txt_mname_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_depart_allowance14)
                    .addComponent(txt_depart_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_lname_allowance10)
                        .addComponent(txt_lname_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(txt_station_empupdt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_lname_allowance11)
                    .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_gender_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_depart_allowance15)
                        .addComponent(txt_clients_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_bday_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_lname_allowance12)
                    .addComponent(lbl_depart_allowance16)
                    .addComponent(combo_contractstatus_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_age_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_lname_allowance13)
                    .addComponent(lbl_depart_allowance17)
                    .addComponent(txt_resignation_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txt_philhealth_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_depart_allowance19)
                    .addComponent(txt_datehired_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txt_pagibig_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_depart_allowance18)
                    .addComponent(txt_lastdayofwork_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txt_SSS_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txt_dateofawol_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txt_tin_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txt_status_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(combo_awol_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_address_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel9)
                    .addComponent(combo_attrition_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nationality_empupdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addContainerGap(108, Short.MAX_VALUE))
        );

        intFrame_employee_update.getContentPane().add(panel_empDetails_payroll4);
        panel_empDetails_payroll4.setBounds(0, 0, 728, 500);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Update Contract");
        intFrame_employee_update.getContentPane().add(jLabel17);
        jLabel17.setBounds(740, 30, 92, 16);

        jLabel16.setText("Contract Start");
        intFrame_employee_update.getContentPane().add(jLabel16);
        jLabel16.setBounds(740, 50, 100, 16);
        intFrame_employee_update.getContentPane().add(txt_contractstart_empupdt);
        txt_contractstart_empupdt.setBounds(850, 50, 181, 22);

        jLabel11.setText("Contract End");
        intFrame_employee_update.getContentPane().add(jLabel11);
        jLabel11.setBounds(740, 90, 100, 22);
        intFrame_employee_update.getContentPane().add(txt_contractend_empupdt);
        txt_contractend_empupdt.setBounds(850, 90, 181, 22);

        cbFirstContract.setText("First Contract");
        cbFirstContract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFirstContractActionPerformed(evt);
            }
        });
        intFrame_employee_update.getContentPane().add(cbFirstContract);
        cbFirstContract.setBounds(740, 130, 130, 20);

        cbSecondContract.setText("Second Contract");
        intFrame_employee_update.getContentPane().add(cbSecondContract);
        cbSecondContract.setBounds(740, 160, 160, 20);

        cbThirdContract.setText("Third Contract");
        intFrame_employee_update.getContentPane().add(cbThirdContract);
        cbThirdContract.setBounds(740, 190, 130, 20);

        getContentPane().add(intFrame_employee_update);
        intFrame_employee_update.setBounds(0, 0, 1200, 680);
        try {
            intFrame_employee_update.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        intFrame_employee_search.setClosable(true);
        intFrame_employee_search.setTitle("Search Employee Details");
        intFrame_employee_search.setMaximumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_search.setMinimumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_search.setPreferredSize(new java.awt.Dimension(800, 500));
        intFrame_employee_search.setVisible(false);

        jScrollPane_tableContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employee Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18), new java.awt.Color(255, 0, 20))); // NOI18N
        jScrollPane_tableContainer.setAutoscrolls(true);
        jScrollPane_tableContainer.setViewportView(jTable4);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Last Name", "First Name", "Middle Name", "Gender", "Birthday", "Age", "Philhealth", "Pag-ibig", "SSS", "TIN", "Address", "Nationality", "Position", "Department", "Station", "Clients", "Contract Status", "Resignation", "Date Hired", "Last Day of Work", "Date of AWOL", "Status", "Attrition", "First Contract", "Second Contract", "Third Contract", "Contract Start", "Contract End", " "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane_tableContainer.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTable4.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable4.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTable4.getColumnModel().getColumn(3).setPreferredWidth(55);
            jTable4.getColumnModel().getColumn(4).setPreferredWidth(75);
            jTable4.getColumnModel().getColumn(5).setPreferredWidth(40);
            jTable4.getColumnModel().getColumn(6).setPreferredWidth(120);
            jTable4.getColumnModel().getColumn(7).setPreferredWidth(120);
            jTable4.getColumnModel().getColumn(8).setPreferredWidth(100);
            jTable4.getColumnModel().getColumn(9).setPreferredWidth(100);
            jTable4.getColumnModel().getColumn(10).setPreferredWidth(400);
            jTable4.getColumnModel().getColumn(11).setPreferredWidth(100);
            jTable4.getColumnModel().getColumn(12).setPreferredWidth(130);
            jTable4.getColumnModel().getColumn(13).setPreferredWidth(150);
            jTable4.getColumnModel().getColumn(15).setPreferredWidth(100);
            jTable4.getColumnModel().getColumn(16).setPreferredWidth(130);
            jTable4.getColumnModel().getColumn(17).setPreferredWidth(100);
            jTable4.getColumnModel().getColumn(18).setPreferredWidth(75);
            jTable4.getColumnModel().getColumn(19).setPreferredWidth(130);
            jTable4.getColumnModel().getColumn(20).setPreferredWidth(100);
            jTable4.getColumnModel().getColumn(21).setPreferredWidth(150);
            jTable4.getColumnModel().getColumn(22).setPreferredWidth(50);
            jTable4.getColumnModel().getColumn(26).setPreferredWidth(100);
            jTable4.getColumnModel().getColumn(27).setPreferredWidth(100);
        }

        btn_searchEmp1.setText("Search Employee Name");
        btn_searchEmp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchEmp1ActionPerformed(evt);
            }
        });

        btn_searchEmp3.setText("Sort by Department");
        btn_searchEmp3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchEmp3ActionPerformed(evt);
            }
        });

        btn_searchEmp4.setText("Sort by Station");
        btn_searchEmp4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchEmp4ActionPerformed(evt);
            }
        });

        btn_searchEmp6.setText("Display AWOL");
        btn_searchEmp6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchEmp6ActionPerformed(evt);
            }
        });

        jButton1.setText("Sort by Status");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btn_searchEmp7.setText("Export");
        btn_searchEmp7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchEmp7ActionPerformed(evt);
            }
        });

        btn_searchEmp5.setText("Sort by Client");
        btn_searchEmp5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchEmp5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout intFrame_employee_searchLayout = new javax.swing.GroupLayout(intFrame_employee_search.getContentPane());
        intFrame_employee_search.getContentPane().setLayout(intFrame_employee_searchLayout);
        intFrame_employee_searchLayout.setHorizontalGroup(
            intFrame_employee_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(intFrame_employee_searchLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(intFrame_employee_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSelectedCount)
                    .addComponent(lblTableCount))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, intFrame_employee_searchLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_searchEmp7, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(176, 176, 176))
            .addGroup(intFrame_employee_searchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(intFrame_employee_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(intFrame_employee_searchLayout.createSequentialGroup()
                        .addComponent(jScrollPane_tableContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 1120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(intFrame_employee_searchLayout.createSequentialGroup()
                        .addComponent(btn_searchEmp1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_searchEmp3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_searchEmp5, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_searchEmp4, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_searchEmp6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(180, 180, 180))))
        );
        intFrame_employee_searchLayout.setVerticalGroup(
            intFrame_employee_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, intFrame_employee_searchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(intFrame_employee_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_searchEmp1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_searchEmp3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_searchEmp4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_searchEmp6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_searchEmp5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane_tableContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTableCount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(intFrame_employee_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSelectedCount)
                    .addComponent(btn_searchEmp7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(122, 122, 122))
        );

        getContentPane().add(intFrame_employee_search);
        intFrame_employee_search.setBounds(0, 0, 1200, 680);
        try {
            intFrame_employee_search.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        intFrame_fourmonths.setClosable(true);
        intFrame_fourmonths.setVisible(false);

        jScrollPane5.setAutoscrolls(true);

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Name", "Position", "Department", "Contract Start", "Contract End"
            }
        ));
        jTable6.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane5.setViewportView(jTable6);
        if (jTable6.getColumnModel().getColumnCount() > 0) {
            jTable6.getColumnModel().getColumn(0).setPreferredWidth(200);
            jTable6.getColumnModel().getColumn(1).setPreferredWidth(150);
            jTable6.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable6.getColumnModel().getColumn(3).setPreferredWidth(180);
            jTable6.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 787, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(395, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout intFrame_fourmonthsLayout = new javax.swing.GroupLayout(intFrame_fourmonths.getContentPane());
        intFrame_fourmonths.getContentPane().setLayout(intFrame_fourmonthsLayout);
        intFrame_fourmonthsLayout.setHorizontalGroup(
            intFrame_fourmonthsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        intFrame_fourmonthsLayout.setVerticalGroup(
            intFrame_fourmonthsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(intFrame_fourmonths);
        intFrame_fourmonths.setBounds(0, 0, 1200, 650);

        intFrame_endofcontract.setClosable(true);
        intFrame_endofcontract.setVisible(false);

        jLabel10.setText("Employees whose contract will end in 1 week or less:");

        jScrollPane3.setAutoscrolls(true);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Name", "Position", "Department", "Contract End", "Days before Contract End"
            }
        ));
        jTable3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane3.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setPreferredWidth(200);
            jTable3.getColumnModel().getColumn(1).setPreferredWidth(150);
            jTable3.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable3.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTable3.getColumnModel().getColumn(4).setPreferredWidth(180);
        }

        jLabel18.setText("Employees whose contract has expired:");

        jScrollPane4.setAutoscrolls(true);

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "Position", "Department", "Contract Expired"
            }
        ));
        jTable5.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane4.setViewportView(jTable5);
        if (jTable5.getColumnModel().getColumnCount() > 0) {
            jTable5.getColumnModel().getColumn(0).setPreferredWidth(200);
            jTable5.getColumnModel().getColumn(1).setPreferredWidth(150);
            jTable5.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable5.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        jLabel3.setForeground(new java.awt.Color(255, 0, 51));
        jLabel3.setText("NOTE: Update their \"Contract Start\" OR \"Status\" using the \"Update Employee\" menu");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 682, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(40, 40, 40))))
        );

        javax.swing.GroupLayout intFrame_endofcontractLayout = new javax.swing.GroupLayout(intFrame_endofcontract.getContentPane());
        intFrame_endofcontract.getContentPane().setLayout(intFrame_endofcontractLayout);
        intFrame_endofcontractLayout.setHorizontalGroup(
            intFrame_endofcontractLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        intFrame_endofcontractLayout.setVerticalGroup(
            intFrame_endofcontractLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(intFrame_endofcontract);
        intFrame_endofcontract.setBounds(0, 0, 1200, 650);

        lbl_pms.setFont(new java.awt.Font("URW Palladio L", 1, 48)); // NOI18N
        lbl_pms.setForeground(new java.awt.Color(36, 121, 158));
        lbl_pms.setText("Employee Monitoring System");
        getContentPane().add(lbl_pms);
        lbl_pms.setBounds(230, 120, 740, 60);

        lbl_background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/forms/images/DOAV_LOGO-removebg-preview.png"))); // NOI18N
        getContentPane().add(lbl_background);
        lbl_background.setBounds(270, -10, 1200, 700);

        menu_menuBar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        menuBar_file.setText("   File   ");
        menuBar_file.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBar_fileActionPerformed(evt);
            }
        });

        menuBar_file_logout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuBar_file_logout.setText("Log Out");
        menuBar_file_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBar_file_logoutActionPerformed(evt);
            }
        });
        menuBar_file.add(menuBar_file_logout);

        menuBar_file_exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuBar_file_exit.setText("Exit");
        menuBar_file_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBar_file_exitActionPerformed(evt);
            }
        });
        menuBar_file.add(menuBar_file_exit);

        menu_menuBar.add(menuBar_file);

        menuBar_employee.setText("   Employee   ");
        menuBar_employee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBar_employeeActionPerformed(evt);
            }
        });

        menuBar_employee_new.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuBar_employee_new.setText("New Employee");
        menuBar_employee_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBar_employee_newActionPerformed(evt);
            }
        });
        menuBar_employee.add(menuBar_employee_new);

        menuBar_employee_update.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuBar_employee_update.setText("Update Employee");
        menuBar_employee_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBar_employee_updateActionPerformed(evt);
            }
        });
        menuBar_employee.add(menuBar_employee_update);

        menuBar_employee_delete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuBar_employee_delete.setText("Delete Employee");
        menuBar_employee_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBar_employee_deleteActionPerformed(evt);
            }
        });
        menuBar_employee.add(menuBar_employee_delete);

        menuBar_employee_search.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuBar_employee_search.setText("Search Employee");
        menuBar_employee_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBar_employee_searchActionPerformed(evt);
            }
        });
        menuBar_employee.add(menuBar_employee_search);

        menu_menuBar.add(menuBar_employee);

        jMenu1.setText("Contract");

        jMenuItem1.setText("Nearing Contract End");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Sort by Months Completed");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        menu_menuBar.add(jMenu1);

        setJMenuBar(menu_menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents
//Change Title bar Icon of the Form

    public void changeIcon() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/icon.png")));
    }

//Disable menu on form initialization    
    public void disableMenu() {

        //menuBar_employee.setEnabled(false);
        //menuBar_payroll.setEnabled(false);
        //menuBar_paySlip.setEnabled(false);
    }

//Add radio buttons to a group
    public void addButtonGroup() {

        //btnGroup_rd.add(rd_male);
        //btnGroup_rd.add(rd_female);
    }

//clear new employee form    
    public void clearEmployeeNew() {
        //txt_empID.setText(null);
        //txt_nic.setText(null);
        //txt_fname.setText(null);
        //txt_lname.setText(null);
        //txt_mname.setText(null);
        //txt_address.setText(null);
        //txt_city.setText(null);
        //txt_dob.setText(null);
        //txt_dateJoin.setText(null);
        //txt_deparment.setText(null);
        //txt_designation.setText(null);
        //txt_telHome.setText(null);
        //txt_telMobile.setText(null);
    }

//clear update employee form    
    public void clearEmployeeUpdate() {
        //txt_empID_update.setText(null);
        //txt_nic_update.setText(null);
       // txt_fname_update.setText(null);
       // txt_mname_update.setText(null);
       // txt_lname_update.setText(null);
        //txt_address_update.setText(null);
        //txt_city_update.setText(null);
        //txt_dob_update.setText(null);
        //txt_dateJoin_update.setText(null);
       // txt_deparment_update.setText(null);
        //txt_designation_update.setText(null);
        //txt_telHome_update.setText(null);
        //txt_telMobile_update.setText(null);

    }
    
    


//validate new employee fields    
    public boolean validateEmployeeNew() {
        /*
        //if (txt_empID.getText().isEmpty()
                //|| txt_nic.getText().isEmpty()
                //|| txt_fname.getText().isEmpty()
                //|| txt_lname.getText().isEmpty()
           //     || txt_dob.getText().isEmpty()
                || txt_designation.getText().isEmpty()
                || txt_deparment.getText().isEmpty())
         //       || txt_dateJoin.getText().isEmpty()) 
        {
            return false;
        } else {
            return true;
        }
         */
        return true;
    }

//validate update employee form fields    
    public boolean validateEmployeeUpdate() {


            return true;
        
    }

//hide frames on opening a new form    
    public void hideFrames() {

        intFrame_employee_new.setVisible(false);
        intFrame_employee_update.setVisible(false);
        intFrame_employee_search.setVisible(false);
        
        intFrame_fourmonths.setVisible(false);
        intFrame_endofcontract.setVisible(false);

    }

//dialog box to get employee id    
    public String getEmpId() {

        return JOptionPane.showInputDialog("Enter Employee ID");

    }

    private void menuBar_fileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_fileActionPerformed

    }//GEN-LAST:event_menuBar_fileActionPerformed

    private void menuBar_file_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_file_exitActionPerformed

        System.exit(0);
    }//GEN-LAST:event_menuBar_file_exitActionPerformed

    private void btn_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exitActionPerformed

        intFrame_employee_new.setVisible(false);
        
    }//GEN-LAST:event_btn_exitActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        String fName = txt_fname_newemp.getText();
        String mName = txt_mname_newemp.getText();
        String lName = txt_lname_newemp.getText();
        String gender = txt_gender_newemp.getText();
        String bday = txt_bday_newemp.getText();
        String age = txt_age_newemp.getText();
        String philhealth = txt_philhealth_newemp.getText();
        String pagibig = txt_pagibig_newemp.getText();
        String sss = txt_SSS_newemp.getText();
        String tin = txt_tin_newemp.getText();
        String address = txt_address_newemp.getText();
        String nationality = txt_nationality_newemp.getText();
        String position = txt_position_newemp.getText();
        String department = txt_depart_newemp.getText();
        String station = txt_station_newemp.getText();
        String clients = txt_clients_newemp.getText();
        String contractStatus = String.valueOf(combo_contractstatus_newemp.getSelectedItem());
        String resignation = txt_resignation_newemp.getText();
        String dateHired = txt_datehired_newemp.getText();
        String lastDayOfWork = txt_lastdayofwork_newemp.getText();
        String dateOfAwol = txt_dateofawol_newemp.getText();
        String status = txt_status_newemp.getText();
        String attrition = String.valueOf(combo_attrition_newemp.getSelectedItem());
        
        String contractStart = dateHired;
        // Given date
        String givenDateStr = dateHired; // Format: yyyy-mm-dd
        LocalDate givenDate = LocalDate.parse(givenDateStr);

        // Calculate 6 months later
        LocalDate sixMonthsLater = givenDate.plusMonths(6);

        // Format the result
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String contractEnd = sixMonthsLater.format(formatter);
        
        SQLRun objSQLRun = new SQLRun();
        String sql = MessageFormat.format("INSERT INTO `empinfo`(`lname`, `fname`, `mname`, `gender`, `birthday`, `age`, `philhealth`, `pagibig`, `sss`, `tin`, `address`, `nationality`, `position`, `department`, `station`, `clients`, `contractstatus`, `resignation`, `datehired`, `lastdayofwork`, `dateofawol`, `status`, `attrition`, `contractstart`, `contractend`) "
                + "VALUES (''{0}'',''{1}'',''{2}'',''{3}'',''{4}'',''{5}'',''{6}'',''{7}'',''{8}'',''{9}'',''{10}'',''{11}'',''{12}'',''{13}'',''{14}'',''{15}'',''{16}'',''{17}'',''{18}'',''{19}'',''{20}'',''{21}'',''{22}'', ''{23}'',''{24}'')", lName, fName, mName, gender, bday, age, philhealth, pagibig, sss, tin, address, nationality, position, department, station, clients, contractStatus, resignation, dateHired, lastDayOfWork, dateOfAwol, status, attrition, contractStart, contractEnd);
        System.out.println(sql);
        int inserted = objSQLRun.sqlUpdate(sql);

        if (inserted > 0) {
            JOptionPane.showMessageDialog(null, "Employee has been added "
                    + "to the system successfully", "Success", 1);
            

        } else {
            JOptionPane.showMessageDialog(null, "Error occurred while trying to add Employee to the system", "ERROR", 0);
            

        }


    }//GEN-LAST:event_btn_addActionPerformed

    private void menuBar_file_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_file_logoutActionPerformed

        Login loginForm = new Login();
        loginForm.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_menuBar_file_logoutActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed

        String fName = txt_fname_empupdt.getText();
        String mName = txt_mname_empupdt.getText();
        String lName = txt_lname_empupdt.getText();
        String gender = txt_gender_empupdt.getText();
        String bday = txt_bday_empupdt.getText();
        String age = txt_age_empupdt.getText();
        String philhealth = txt_philhealth_empupdt.getText();
        String pagibig = txt_pagibig_empupdt.getText();
        String sss = txt_SSS_empupdt.getText();
        String tin = txt_tin_empupdt.getText();
        String address = txt_address_empupdt.getText();
        String nationality = txt_nationality_empupdt.getText();
        String position = txt_position_empupdt.getText();
        String department = txt_depart_empupdt.getText();
        String station = txt_station_empupdt.getText();
        String clients = txt_clients_empupdt.getText();
        String contractStatus = String.valueOf(combo_contractstatus_empupdt.getSelectedItem());
        String resignation = txt_resignation_empupdt.getText();
        String dateHired = txt_datehired_empupdt.getText();
        String lastDayOfWork = txt_lastdayofwork_empupdt.getText();
        String dateOfAwol = txt_dateofawol_empupdt.getText();
        String status = txt_status_empupdt.getText();
        String contractStart = txt_contractstart_empupdt.getText();
        String contractEnd = txt_contractend_empupdt.getText();
        String attrition = String.valueOf(combo_attrition_empupdt.getSelectedItem());
        String awol = String.valueOf(combo_awol_empupdt.getSelectedItem());
        
        
        // Retrieve the states of the checkboxes
        boolean firstContract = cbFirstContract.isSelected();
        boolean secondContract = cbSecondContract.isSelected();
        boolean thirdContract = cbThirdContract.isSelected();

        // Convert boolean states to integers (1 for true, 0 for false)
        int firstContractValue = firstContract ? 1 : 0;
        int secondContractValue = secondContract ? 1 : 0;
        int thirdContractValue = thirdContract ? 1 : 0;
        
        SQLRun objSQLRun = new SQLRun();
        String sql = MessageFormat.format("UPDATE `empinfo` SET `lname`=''{0}'',`fname`=''{1}'',`mname`=''{2}'',`gender`=''{3}'',`birthday`=''{4}'',`age`=''{5}'',`philhealth`=''{6}'',`pagibig`=''{7}'',`sss`=''{8}'',`tin`=''{9}'',`address`=''{10}'',`nationality`=''{11}'',`position`=''{12}'',`department`=''{13}'', `station` =''{14}'', `clients`=''{15}'',`contractstatus`=''{16}'',`resignation`=''{17}'',`datehired`=''{18}'',`lastdayofwork`=''{19}'',`dateofawol`=''{20}'',`status`=''{21}'',`attrition`=''{22}'', `firstcontract`=''{23}'', `secondcontract`=''{24}'', `thirdcontract`=''{25}'',`contractstart` = ''{26}'', `contractend`=''{27}'', `awol` = ''{28}'' WHERE `id` = ''{29}''", 
                lName, fName, mName, gender, bday, age, philhealth, pagibig, sss, tin, address, nationality, position, department, station, clients, contractStatus, resignation, dateHired, lastDayOfWork, dateOfAwol, status, attrition, firstContractValue, secondContractValue, thirdContractValue, contractStart, contractEnd, awol, id);
        
        int inserted = objSQLRun.sqlUpdate(sql);

        if (inserted > 0) {
            JOptionPane.showMessageDialog(null, "Employee information has been updated successfully", "Success", 1);
            

        } else {
            JOptionPane.showMessageDialog(null, "Error occurred while trying to update Employee information", "ERROR", 0);
            

        }
        notifyContractEndCheck();
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_exit_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exit_updateActionPerformed

        intFrame_employee_update.setVisible(false);
       
    }//GEN-LAST:event_btn_exit_updateActionPerformed

    private void btn_search_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_search_updateActionPerformed
            jDialog10.show();
        

        
    }//GEN-LAST:event_btn_search_updateActionPerformed


    private void leave_empfnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leave_empfnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_leave_empfnameActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void leave_emplnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leave_emplnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_leave_emplnameActionPerformed

    private void btn_searchEmp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchEmp1ActionPerformed
        jDialog4.setVisible(true);

    }//GEN-LAST:event_btn_searchEmp1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //String empfname = employee_empfname1.getText();
        String emplname = employee_emplname1.getText();
        String sql = "SELECT * FROM empinfo WHERE  lname ='" + emplname + "' ";
        /*empDetails = new JTable(objEmployee.getAllEmployeeDetails(objEmployee.getAllEmployeeDetails(sql)), objEmployee.getColumnNames(objEmployee.getAllEmployeeDetails(sql)));
        
        jScrollPane_tableContainer.setViewportView(empDetails);
        */
        loadData(sql);
        jDialog4.setVisible(false);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void employee_emplname1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_emplname1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_emplname1ActionPerformed

    private void employee_desigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_desigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_desigActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        String desig = employee_desig.getText();
        String sql = "SELECT * FROM employee WHERE designation='" + desig + "'";
        JTable empDetails = new JTable(objEmployee.getAllEmployeeDetails(objEmployee.getAllEmployeeDetails(sql)), objEmployee.getColumnNames(objEmployee.getAllEmployeeDetails(sql)));
        jScrollPane_tableContainer.setViewportView(empDetails);
        jDialog5.setVisible(false);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void btn_searchEmp3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchEmp3ActionPerformed
        jDialog17.setVisible(true);
    }//GEN-LAST:event_btn_searchEmp3ActionPerformed

    private void employee_deptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_deptActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_deptActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        String dept = employee_dept.getText();
        String sql = "SELECT * FROM empinfo WHERE department='" + dept + "'";
        /*JTable empDetails = new JTable(objEmployee.getAllEmployeeDetails(objEmployee.getAllEmployeeDetails(sql)), objEmployee.getColumnNames(objEmployee.getAllEmployeeDetails(sql)));
        jScrollPane_tableContainer.setViewportView(empDetails);*/
        loadData(sql);
        jDialog6.setVisible(false);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void log_empfnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_log_empfnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_log_empfnameActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        //model3 = (DefaultTableModel) jTable1.getModel();
        String logfname = log_empfname.getText();
        String loglname = log_emplname.getText();
        loadData("SELECT * FROM violationlog WHERE `First Name` = '" + logfname + "' AND `Last Name` = '" + loglname + "'");

        jDialog7.setVisible(false);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void log_emplnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_log_emplnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_log_emplnameActionPerformed

    private void log_empfname1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_log_empfname1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_log_empfname1ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        //model3 = (DefaultTableModel) jTable2.getModel();
        String logfname = log_empfname1.getText();
        String loglname = log_emplname1.getText();
        //loadDataUniform("SELECT * FROM uniformlog WHERE fname = '" + logfname + "' AND lname = '" + loglname + "'");

        jDialog7.setVisible(false);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void log_emplname1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_log_emplname1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_log_emplname1ActionPerformed

    private void menuBar_employee_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_employee_newActionPerformed

        hideFrames();
        intFrame_employee_new.setVisible(true);
    }//GEN-LAST:event_menuBar_employee_newActionPerformed

    private void menuBar_employee_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_employee_updateActionPerformed

        hideFrames();
        intFrame_employee_update.setVisible(true);
    }//GEN-LAST:event_menuBar_employee_updateActionPerformed

    private void menuBar_employee_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_employee_deleteActionPerformed

        hideFrames();
        jDialog9.show();
    }//GEN-LAST:event_menuBar_employee_deleteActionPerformed

    private void menuBar_employee_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_employee_searchActionPerformed
        //populate table with employee details
        hideFrames();
        intFrame_employee_search.setVisible(true);
        searchEmployee = (DefaultTableModel) jTable4.getModel();
        String sql = "SELECT * FROM empinfo";
        loadData(sql);
        updateEntryCountLabel(lblTableCount, searchEmployee);
        searchEmployee.addTableModelListener(e -> updateEntryCountLabel(lblTableCount, searchEmployee));
        updateSelectedCountLabel(lblSelectedCount, jTable4);
        jTable4.getSelectionModel().addListSelectionListener(e -> updateSelectedCountLabel(lblSelectedCount, jTable4));
        /*String sql = "SELECT * FROM empinfo";
        JTable empDetails = new JTable(objEmployee.getAllEmployeeDetails(objEmployee.getAllEmployeeDetails(sql)), objEmployee.getColumnNames(objEmployee.getAllEmployeeDetails(sql)));
        jScrollPane_tableContainer.setViewportView(empDetails);
        */
        
    }//GEN-LAST:event_menuBar_employee_searchActionPerformed

    private void menuBar_employeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_employeeActionPerformed

    }//GEN-LAST:event_menuBar_employeeActionPerformed

    private void del_employee_empfname2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_del_employee_empfname2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_del_employee_empfname2ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        String delfname = del_employee_empfname2.getText();
        String dellname = del_employee_emplname2.getText();
        SQLRun objSQLRun = new SQLRun();
        String sql = "DELETE FROM empinfo WHERE fname='" + delfname + "' AND lname ='" + dellname + "'";

        int deleted = objSQLRun.sqlUpdate(sql);

        if (deleted > 0) {
            JOptionPane.showMessageDialog(null, "Employee " + delfname + " " + dellname + " has been deleted successfully", "ERROR", 1);
            jDialog9.dispose();

        } else {
            
                JOptionPane.showMessageDialog(null, "Employee " + delfname + " " + dellname + " does not exist", "ERROR", 0);
                
            
        }
        
    }//GEN-LAST:event_jButton18ActionPerformed

    private void del_employee_emplname2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_del_employee_emplname2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_del_employee_emplname2ActionPerformed

    private void updt_empfname2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updt_empfname2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updt_empfname2ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        String updtempfname = updt_empfname2.getText();
        String updtemplname = updt_emplname2.getText();
        SQLRun objSQLRun = new SQLRun();
        try {
            String sql = "SELECT * FROM empinfo WHERE fname = '" + updtempfname + "' AND lname = '" + updtemplname + "'";

            ResultSet rs = objSQLRun.sqlQuery(sql);

            if (rs.next()) {

                id = rs.getInt("id");
                String fName = rs.getString("fname");
                String mName = rs.getString("mname");
                String lName = rs.getString("lname");
                String gender = rs.getString("gender");
                String bday = rs.getString("birthday");
                String age = rs.getString("age");
                
                String philhealth = rs.getString("philhealth");
                String pagibig = rs.getString("pagibig");
                String sss = rs.getString("sss");
                String tin = rs.getString("tin");
                String address = rs.getString("address");
                String nationality = rs.getString("nationality");
                String position = rs.getString("position");
                String department = rs.getString("department");
                String station = rs.getString("station");
                
                String clients = rs.getString("clients");
                String contractStatus = rs.getString("contractstatus");
                String resignation = rs.getString("resignation");
                String dateHired = rs.getString("datehired");
                String lastDayOfWork = rs.getString("lastdayofwork");
                String dateOfAwol = rs.getString("dateofawol");
                String status = rs.getString("status");
                String attrition = rs.getString("attrition");
                String contractStart = rs.getString("contractstart");
                String contractEnd = rs.getString("contractend");
                String awol = rs.getString("awol");
                
                // Retrieve the states of the checkboxes
                boolean firstContract = rs.getBoolean("firstcontract");
                boolean secondContract = rs.getBoolean("secondcontract");
                boolean thirdContract = rs.getBoolean("thirdcontract");

                
                txt_fname_empupdt.setText(fName);
                txt_mname_empupdt.setText(mName);
                txt_lname_empupdt.setText(lName);
                txt_gender_empupdt.setText(gender);
                txt_bday_empupdt.setText(bday);
                String newAge = String.valueOf(calculateAgeViaBirthdate(txt_bday_empupdt.getText()));
                txt_age_empupdt.setText(newAge);
                //txt_age_empupdt.setText(age);
                txt_philhealth_empupdt.setText(philhealth);
                txt_pagibig_empupdt.setText(pagibig);
                txt_SSS_empupdt.setText(sss);
                txt_tin_empupdt.setText(tin);
                txt_address_empupdt.setText(address);
                txt_nationality_empupdt.setText(nationality);
                txt_position_empupdt.setText(position);
                txt_depart_empupdt.setText(department);
                txt_station_empupdt.setText(station);
                txt_clients_empupdt.setText(clients);
                Object contractStatusObj = contractStatus;
                combo_contractstatus_empupdt.setSelectedItem(contractStatusObj);
                txt_resignation_empupdt.setText(resignation);
                txt_datehired_empupdt.setText(dateHired);
                txt_lastdayofwork_empupdt.setText(lastDayOfWork);
                txt_dateofawol_empupdt.setText(dateOfAwol);
                txt_status_empupdt.setText(status);
                Object attritionObj = attrition;
                combo_attrition_empupdt.setSelectedItem(attritionObj);
                txt_contractstart_empupdt.setText(contractStart);
                txt_contractend_empupdt.setText(contractEnd);
                Object awolObj = awol;
                combo_awol_empupdt.setSelectedItem(awolObj);
                
                // Set the states of the checkboxes
                cbFirstContract.setSelected(firstContract);
                cbSecondContract.setSelected(secondContract);
                cbThirdContract.setSelected(thirdContract);
                
                jDialog10.dispose();
            } else {

                JOptionPane.showMessageDialog(null, "No record found ", "ERROR", 0);
                

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Failed to Retrieve Data! Please Contact Your System Administrator!\n\n" + ex.getMessage(), "ERROR", 0);
            
        }
        
    }//GEN-LAST:event_jButton19ActionPerformed

    private void updt_emplname2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updt_emplname2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updt_emplname2ActionPerformed

    private void benefitssearch_empfname3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_benefitssearch_empfname3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_benefitssearch_empfname3ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        
    }//GEN-LAST:event_jButton21ActionPerformed

    private void benefitssearch_emplname3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_benefitssearch_emplname3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_benefitssearch_emplname3ActionPerformed

    private void txt_lname_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_lname_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_lname_newempActionPerformed

    private void txt_SSS_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_SSS_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SSS_newempActionPerformed

    private void txt_philhealth_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_philhealth_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_philhealth_newempActionPerformed

    private void txt_pagibig_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_pagibig_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pagibig_newempActionPerformed

    private void txt_tin_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tin_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tin_newempActionPerformed

    private void txt_gender_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_gender_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_gender_newempActionPerformed

    private void txt_bday_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_bday_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_bday_newempActionPerformed

    private void txt_age_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_age_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_age_newempActionPerformed

    private void txt_address_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_address_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_address_newempActionPerformed

    private void txt_nationality_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nationality_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nationality_newempActionPerformed

    private void txt_datehired_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_datehired_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_datehired_newempActionPerformed

    private void txt_lname_empupdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_lname_empupdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_lname_empupdtActionPerformed

    private void txt_SSS_empupdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_SSS_empupdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SSS_empupdtActionPerformed

    private void txt_philhealth_empupdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_philhealth_empupdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_philhealth_empupdtActionPerformed

    private void txt_pagibig_empupdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_pagibig_empupdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pagibig_empupdtActionPerformed

    private void txt_tin_empupdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tin_empupdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tin_empupdtActionPerformed

    private void txt_gender_empupdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_gender_empupdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_gender_empupdtActionPerformed

    private void txt_bday_empupdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_bday_empupdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_bday_empupdtActionPerformed

    private void txt_age_empupdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_age_empupdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_age_empupdtActionPerformed

    private void txt_address_empupdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_address_empupdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_address_empupdtActionPerformed

    private void txt_nationality_empupdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nationality_empupdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nationality_empupdtActionPerformed

    private void txt_datehired_empupdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_datehired_empupdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_datehired_empupdtActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        hideFrames();
        intFrame_endofcontract.setVisible(true);
        notifyContractEndCheck();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        hideFrames();
        jDialog13.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void txt_station_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_station_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_station_newempActionPerformed

    private void btn_searchEmp4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchEmp4ActionPerformed
        jDialog16.setVisible(true);
    }//GEN-LAST:event_btn_searchEmp4ActionPerformed

    private void station_txtfldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_station_txtfldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_station_txtfldActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        String station = station_txtfld.getText();
        String sql = "SELECT * FROM empinfo WHERE station='" + station + "'";
        /*JTable empDetails = new JTable(objEmployee.getAllEmployeeDetails(objEmployee.getAllEmployeeDetails(sql)), objEmployee.getColumnNames(objEmployee.getAllEmployeeDetails(sql)));
        jScrollPane_tableContainer.setViewportView(empDetails);*/
        loadData(sql);
        jDialog12.setVisible(false);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void monthsCompleted_txtfld1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthsCompleted_txtfld1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_monthsCompleted_txtfld1ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        monthsCompleted = Integer.parseInt(monthsCompleted_txtfld1.getText());
        intFrame_fourmonths.setVisible(true);
        sortByMonthsDone();
        jDialog13.setVisible(false);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void status_txtfld2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_status_txtfld2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_status_txtfld2ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        String status = status_txtfld2.getText();
        String sql = "SELECT * FROM empinfo WHERE status='" + status + "'";
        /*JTable empDetails = new JTable(objEmployee.getAllEmployeeDetails(objEmployee.getAllEmployeeDetails(sql)), objEmployee.getColumnNames(objEmployee.getAllEmployeeDetails(sql)));
        jScrollPane_tableContainer.setViewportView(empDetails);*/
        loadData(sql);
        jDialog14.setVisible(false);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void combo_contractstatus_newempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_contractstatus_newempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_contractstatus_newempActionPerformed

    private void btn_searchEmp6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchEmp6ActionPerformed
        String sql = "SELECT * FROM empinfo WHERE awol='AWOL'";
        loadData(sql);
    }//GEN-LAST:event_btn_searchEmp6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jDialog15.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbActiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbActiveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbActiveActionPerformed

    private void cbRenderingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRenderingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbRenderingActionPerformed

    private void cbManilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbManilaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbManilaActionPerformed

    private void cbCebuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCebuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCebuActionPerformed

    private void cbBuildingMaintenanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBuildingMaintenanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbBuildingMaintenanceActionPerformed

    private void cbExecutiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbExecutiveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbExecutiveActionPerformed

    private void cbFinanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFinanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbFinanceActionPerformed

    private void cbGroundHandlingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbGroundHandlingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbGroundHandlingActionPerformed

    private void cbGSEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbGSEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbGSEActionPerformed

    private void cbHRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbHRActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbHRActionPerformed

    private void cbPestControlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPestControlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbPestControlActionPerformed

    private void cbMaintenanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMaintenanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbMaintenanceActionPerformed

    private void cbTechnicalRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTechnicalRecordsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTechnicalRecordsActionPerformed

    private void cbOCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOCCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbOCCActionPerformed

    private void cbShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbShopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbShopActionPerformed

    private void cbOperationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOperationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbOperationActionPerformed

    private void cbToolRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbToolRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbToolRoomActionPerformed

    private void cbOGMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOGMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbOGMActionPerformed

    private void cbQualityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbQualityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbQualityActionPerformed

    private void cbWarehouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbWarehouseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbWarehouseActionPerformed

    private void cbTrainingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTrainingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTrainingActionPerformed

    private void cbFirstContractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFirstContractActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbFirstContractActionPerformed

    private void btn_searchEmp7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchEmp7ActionPerformed
        // TODO add your handling code here:
        exportCertain(jTable4);
    }//GEN-LAST:event_btn_searchEmp7ActionPerformed

    private void btn_searchEmp5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchEmp5ActionPerformed
        jDialog18.setVisible(true);
    }//GEN-LAST:event_btn_searchEmp5ActionPerformed

    private void cbAirAsiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAirAsiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbAirAsiaActionPerformed

    private void cbCebuPacificCebuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCebuPacificCebuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCebuPacificCebuActionPerformed

    private void cbCebuPacificClarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCebuPacificClarkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCebuPacificClarkActionPerformed

    private void cbDotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbDotaActionPerformed

    private void cbDotaCebuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDotaCebuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbDotaCebuActionPerformed

    private void cbDotaClarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDotaClarkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbDotaClarkActionPerformed

    private void cbMacroAsiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMacroAsiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbMacroAsiaActionPerformed

    private void cbPalexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPalexActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbPalexActionPerformed

    private void cbPalexCebuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPalexCebuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbPalexCebuActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    class PrintableImage implements Printable {

        private BufferedImage image;

        PrintableImage(BufferedImage image) {
            this.image = image;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) graphics;
            double scaleX = pageFormat.getImageableWidth() / image.getWidth();
            double scaleY = pageFormat.getImageableHeight() / image.getHeight();
            double scaleFactor = Math.min(scaleX, scaleY);
            int scaledWidth = (int) (image.getWidth() * scaleFactor);
            int scaledHeight = (int) (image.getHeight() * scaleFactor);

            // Center the image on the page
            int x = (int) ((pageFormat.getImageableWidth() - scaledWidth) / 2);
            int y = (int) ((pageFormat.getImageableHeight() - scaledHeight) / 2);

            g2d.drawImage(image, x, y, scaledWidth, scaledHeight, null);
            return PAGE_EXISTS;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField benefitssearch_empfname3;
    private javax.swing.JTextField benefitssearch_emplname3;
    private javax.swing.ButtonGroup btnGroup_rd;
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_exit;
    private javax.swing.JButton btn_exit_update;
    private javax.swing.JButton btn_searchEmp1;
    private javax.swing.JButton btn_searchEmp3;
    private javax.swing.JButton btn_searchEmp4;
    private javax.swing.JButton btn_searchEmp5;
    private javax.swing.JButton btn_searchEmp6;
    private javax.swing.JButton btn_searchEmp7;
    private javax.swing.JButton btn_search_update;
    private javax.swing.JButton btn_update;
    private javax.swing.JCheckBox cbActive;
    private javax.swing.JCheckBox cbAirAsia;
    private javax.swing.JCheckBox cbAirswift;
    private javax.swing.JCheckBox cbBuildingMaintenance;
    private javax.swing.JCheckBox cbCebu;
    private javax.swing.JCheckBox cbCebuPacific;
    private javax.swing.JCheckBox cbCebuPacificCebu;
    private javax.swing.JCheckBox cbCebuPacificClark;
    private javax.swing.JCheckBox cbClark;
    private javax.swing.JCheckBox cbCleaning;
    private javax.swing.JCheckBox cbDota;
    private javax.swing.JCheckBox cbDotaCebu;
    private javax.swing.JCheckBox cbDotaClark;
    private javax.swing.JCheckBox cbExecutive;
    private javax.swing.JCheckBox cbFinance;
    private javax.swing.JCheckBox cbFirstContract;
    private javax.swing.JCheckBox cbGSE;
    private javax.swing.JCheckBox cbGroundHandling;
    private javax.swing.JCheckBox cbHR;
    private javax.swing.JCheckBox cbMacroAsia;
    private javax.swing.JCheckBox cbMaintenance;
    private javax.swing.JCheckBox cbManila;
    private javax.swing.JCheckBox cbNLC;
    private javax.swing.JCheckBox cbOCC;
    private javax.swing.JCheckBox cbOGM;
    private javax.swing.JCheckBox cbOperation;
    private javax.swing.JCheckBox cbPalex;
    private javax.swing.JCheckBox cbPalexCebu;
    private javax.swing.JCheckBox cbPestControl;
    private javax.swing.JCheckBox cbQuality;
    private javax.swing.JCheckBox cbRendering;
    private javax.swing.JCheckBox cbSecondContract;
    private javax.swing.JCheckBox cbSelectAll;
    private javax.swing.JCheckBox cbSelectAllClients;
    private javax.swing.JCheckBox cbSelectAllDepartments;
    private javax.swing.JCheckBox cbSelectAllStation;
    private javax.swing.JCheckBox cbShop;
    private javax.swing.JCheckBox cbTechnicalRecords;
    private javax.swing.JCheckBox cbThirdContract;
    private javax.swing.JCheckBox cbToolRoom;
    private javax.swing.JCheckBox cbTraining;
    private javax.swing.JCheckBox cbWarehouse;
    private javax.swing.JCheckBox cbWithdrawn;
    private javax.swing.JComboBox<String> combo_attrition_empupdt;
    private javax.swing.JComboBox<String> combo_attrition_newemp;
    private javax.swing.JComboBox<String> combo_awol_empupdt;
    private javax.swing.JComboBox<String> combo_contractstatus_empupdt;
    private javax.swing.JComboBox<String> combo_contractstatus_newemp;
    private javax.swing.JTextField del_employee_empfname2;
    private javax.swing.JTextField del_employee_emplname2;
    private javax.swing.JTextField employee_dept;
    private javax.swing.JTextField employee_desig;
    private javax.swing.JTextField employee_emplname1;
    private javax.swing.JInternalFrame intFrame_employee_new;
    private javax.swing.JInternalFrame intFrame_employee_search;
    private javax.swing.JInternalFrame intFrame_employee_update;
    private javax.swing.JInternalFrame intFrame_endofcontract;
    private javax.swing.JInternalFrame intFrame_fourmonths;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JDialog jDialog10;
    private javax.swing.JDialog jDialog11;
    private javax.swing.JDialog jDialog12;
    private javax.swing.JDialog jDialog13;
    private javax.swing.JDialog jDialog14;
    private javax.swing.JDialog jDialog15;
    private javax.swing.JDialog jDialog16;
    private javax.swing.JDialog jDialog17;
    private javax.swing.JDialog jDialog18;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JDialog jDialog5;
    private javax.swing.JDialog jDialog6;
    private javax.swing.JDialog jDialog7;
    private javax.swing.JDialog jDialog8;
    private javax.swing.JDialog jDialog9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane_tableContainer;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JLabel lblSelectedCount;
    private javax.swing.JLabel lblTableCount;
    private javax.swing.JLabel lbl_background;
    private javax.swing.JLabel lbl_depart_allowance10;
    private javax.swing.JLabel lbl_depart_allowance11;
    private javax.swing.JLabel lbl_depart_allowance12;
    private javax.swing.JLabel lbl_depart_allowance13;
    private javax.swing.JLabel lbl_depart_allowance14;
    private javax.swing.JLabel lbl_depart_allowance15;
    private javax.swing.JLabel lbl_depart_allowance16;
    private javax.swing.JLabel lbl_depart_allowance17;
    private javax.swing.JLabel lbl_depart_allowance18;
    private javax.swing.JLabel lbl_depart_allowance19;
    private javax.swing.JLabel lbl_depart_allowance8;
    private javax.swing.JLabel lbl_depart_allowance9;
    private javax.swing.JLabel lbl_desig_allowance3;
    private javax.swing.JLabel lbl_desig_allowance4;
    private javax.swing.JLabel lbl_fname_allowance4;
    private javax.swing.JLabel lbl_fname_allowance5;
    private javax.swing.JLabel lbl_fname_allowance6;
    private javax.swing.JLabel lbl_fname_allowance7;
    private javax.swing.JLabel lbl_lname_allowance10;
    private javax.swing.JLabel lbl_lname_allowance11;
    private javax.swing.JLabel lbl_lname_allowance12;
    private javax.swing.JLabel lbl_lname_allowance13;
    private javax.swing.JLabel lbl_lname_allowance6;
    private javax.swing.JLabel lbl_lname_allowance7;
    private javax.swing.JLabel lbl_lname_allowance8;
    private javax.swing.JLabel lbl_lname_allowance9;
    private javax.swing.JLabel lbl_pms;
    private javax.swing.JTextField leave_empfname;
    private javax.swing.JTextField leave_emplname;
    private javax.swing.JTextField log_empfname;
    private javax.swing.JTextField log_empfname1;
    private javax.swing.JTextField log_emplname;
    private javax.swing.JTextField log_emplname1;
    private javax.swing.JMenu menuBar_employee;
    private javax.swing.JMenuItem menuBar_employee_delete;
    private javax.swing.JMenuItem menuBar_employee_new;
    private javax.swing.JMenuItem menuBar_employee_search;
    private javax.swing.JMenuItem menuBar_employee_update;
    private javax.swing.JMenu menuBar_file;
    private javax.swing.JMenuItem menuBar_file_exit;
    private javax.swing.JMenuItem menuBar_file_logout;
    private javax.swing.JMenuBar menu_menuBar;
    private javax.swing.JTextField monthsCompleted_txtfld1;
    private javax.swing.JPanel panel_empDetails_payroll3;
    private javax.swing.JPanel panel_empDetails_payroll4;
    private javax.swing.JTextField station_txtfld;
    private javax.swing.JTextField status_txtfld2;
    private javax.swing.JTextField txt_SSS_empupdt;
    private javax.swing.JTextField txt_SSS_newemp;
    private javax.swing.JTextField txt_address_empupdt;
    private javax.swing.JTextField txt_address_newemp;
    private javax.swing.JTextField txt_age_empupdt;
    private javax.swing.JTextField txt_age_newemp;
    private javax.swing.JTextField txt_bday_empupdt;
    private javax.swing.JTextField txt_bday_newemp;
    private javax.swing.JTextField txt_clients_empupdt;
    private javax.swing.JTextField txt_clients_newemp;
    private javax.swing.JTextField txt_contractend_empupdt;
    private javax.swing.JTextField txt_contractstart_empupdt;
    private javax.swing.JTextField txt_datehired_empupdt;
    private javax.swing.JTextField txt_datehired_newemp;
    private javax.swing.JTextField txt_dateofawol_empupdt;
    private javax.swing.JTextField txt_dateofawol_newemp;
    private javax.swing.JTextField txt_depart_empupdt;
    private javax.swing.JTextField txt_depart_newemp;
    private javax.swing.JTextField txt_fname_empupdt;
    private javax.swing.JTextField txt_fname_newemp;
    private javax.swing.JTextField txt_gender_empupdt;
    private javax.swing.JTextField txt_gender_newemp;
    private javax.swing.JTextField txt_lastdayofwork_empupdt;
    private javax.swing.JTextField txt_lastdayofwork_newemp;
    private javax.swing.JTextField txt_lname_empupdt;
    private javax.swing.JTextField txt_lname_newemp;
    private javax.swing.JTextField txt_mname_empupdt;
    private javax.swing.JTextField txt_mname_newemp;
    private javax.swing.JTextField txt_nationality_empupdt;
    private javax.swing.JTextField txt_nationality_newemp;
    private javax.swing.JTextField txt_pagibig_empupdt;
    private javax.swing.JTextField txt_pagibig_newemp;
    private javax.swing.JTextField txt_philhealth_empupdt;
    private javax.swing.JTextField txt_philhealth_newemp;
    private javax.swing.JTextField txt_position_empupdt;
    private javax.swing.JTextField txt_position_newemp;
    private javax.swing.JTextField txt_resignation_empupdt;
    private javax.swing.JTextField txt_resignation_newemp;
    private javax.swing.JTextField txt_station_empupdt;
    private javax.swing.JTextField txt_station_newemp;
    private javax.swing.JTextField txt_status_empupdt;
    private javax.swing.JTextField txt_status_newemp;
    private javax.swing.JTextField txt_tin_empupdt;
    private javax.swing.JTextField txt_tin_newemp;
    private javax.swing.JTextField updt_empfname2;
    private javax.swing.JTextField updt_emplname2;
    // End of variables declaration//GEN-END:variables
}
