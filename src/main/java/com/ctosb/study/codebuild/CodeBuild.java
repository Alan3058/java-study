package com.ctosb.study.codebuild;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeBuild {

    private JFrame frame;
    private JTable table;
    private JPanel panel;
    private JCheckBox chckbxController;
    private JCheckBox chckbxDao;
    private JCheckBox chckbxService;
    private JCheckBox chckbxJsp;
    private JButton button;
    private JCheckBox chckbxModel;
    private JTextField moduleText;
    private JLabel label;
    private JPanel panel_1;
    private JPanel panel_2;

    private String url, user, pwd;
    private Connection conn;
    private DatabaseMetaData dbmd;

    private Log log = LogFactory.getLog(CodeBuild.class);
    private JCheckBox chckbxJs;

    /**
     * Launch the application.
     *
     * @throws UnsupportedLookAndFeelException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CodeBuild window = new CodeBuild();
//					window.frame.pack();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public CodeBuild() {
        initialize();
        initdata();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.rowHeights = new int[]{146, 0};
        gridBagLayout.columnWeights = new double[]{1.0};
        gridBagLayout.rowWeights = new double[]{1.0, 1.0};
        frame.getContentPane().setLayout(gridBagLayout);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        frame.getContentPane().add(scrollPane, gbc_scrollPane);

        table = new JTable() {

            @Override
            public boolean isCellEditable(int row, int column) {
                // TODO Auto-generated method stub
                return false;
            }

        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "\u8868\u540D\u79F0", "\u63CF\u8FF0", "\u7C7B\u578B"
                }
        ));
        scrollPane.setViewportView(table);

        panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.anchor = GridBagConstraints.WEST;
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.fill = GridBagConstraints.VERTICAL;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 1;
        frame.getContentPane().add(panel, gbc_panel);

        panel_1 = new JPanel();
        panel.add(panel_1);

        chckbxJs = new JCheckBox("Js");
        panel_1.add(chckbxJs);

        chckbxJsp = new JCheckBox("Jsp");
        panel_1.add(chckbxJsp);

        chckbxController = new JCheckBox("Controller");
        panel_1.add(chckbxController);

        chckbxService = new JCheckBox("Service");
        panel_1.add(chckbxService);

        chckbxDao = new JCheckBox("Dao");
        panel_1.add(chckbxDao);

        chckbxModel = new JCheckBox("Model");
        panel_1.add(chckbxModel);

        panel_2 = new JPanel();
        panel.add(panel_2);

        label = new JLabel("模块名");
        panel_2.add(label);

        moduleText = new JTextField();
        panel_2.add(moduleText);
        moduleText.setColumns(30);

        button = new JButton("\u751F\u6210");
        panel_2.add(button);
    }

    /**
     * 初始化数据
     *
     * @author alan
     * @date 2014-8-12 下午3:53:32
     */
    private void initdata() {
        initDriver();
        initTable();
        chckbxDao.setVisible(false);
        chckbxJs.setSelected(true);
        chckbxJsp.setSelected(true);
        chckbxController.setSelected(true);
        chckbxService.setSelected(true);
        chckbxModel.setSelected(true);
        //监听生成按钮
        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                super.mouseReleased(e);
                build();
            }

        });
    }

    /**
     * 初始化jdbc连接驱动
     *
     * @author alan
     * @date 2014-8-12 下午3:54:07
     */
    private void initDriver() {
        url = CodeResourceBundleUtil.getUrl();
        user = CodeResourceBundleUtil.getUseName();
        pwd = CodeResourceBundleUtil.getPassword();
        try {
            // 加载驱动，这一句也可写为：Class.forName("com.mysql.jdbc.Driver");
            Class.forName(CodeResourceBundleUtil.getDriver()).newInstance();
            // 建立到MySQL的连接
            conn = DriverManager.getConnection(url, user, pwd);
            dbmd = conn.getMetaData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Jtable信息
     *
     * @author alan
     * @date 2014-8-12 下午3:54:39
     */
    private void initTable() {
        try {
            ResultSet rs = dbmd.getTables(null, null, null, null);
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            while (rs.next()) {
                // String tableName = rs.getString("TABLE_NAME"); // 表名
                // String tableType = rs.getString("TABLE_TYPE"); // 表类型
                // String remarks = rs.getString("REMARKS"); // 表备注
                tableModel.addRow(new Object[]{rs.getString("TABLE_NAME"),
                        rs.getString("REMARKS"), rs.getString("TABLE_TYPE")});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成代码
     *
     * @author alan
     * @date 2014-8-13 下午4:26:22
     */
    private void build() {
        int rowId = table.getSelectedRow();
        if (rowId == -1) {
            JOptionPane.showMessageDialog(frame, "请选择一行");
            return;
        }
        String tableName = (String) table.getValueAt(rowId, 0);//表名
        String tableRemark = (String) table.getValueAt(rowId, 1);//表注释

        String className = CamelCaseUtils.toCapitalizeCamelCase(tableName);//类名
        String miniClassName = CamelCaseUtils.toCamelCase(tableName);//首字母小写类名
        String moduleName = moduleText.getText().trim();//模块名
        String packageName;
        if ("".equals(moduleName)) {
            packageName = CodeResourceBundleUtil.getBuildPackage().replace('\\', '.');//包名
        } else {
            packageName = CodeResourceBundleUtil.getBuildPackage().replace('\\', '.') + "." + moduleName;//包名
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("packageName", packageName);
        map.put("tableName", tableName);
        map.put("tableRemark", tableRemark);
        map.put("className", className);
        map.put("miniClassName", miniClassName);
        map.putAll(getTableMessage(tableName));
        //项目路径
        String path = System.getProperty("user.dir");
        //模版路径
        String templatePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "/" + CodeResourceBundleUtil.getTemplatePath();
        //Java文件输出路径
        String outPath = path + "\\" + CodeResourceBundleUtil.getJavaSourceFolder() + "\\" + CodeResourceBundleUtil.getBuildPackage() + "\\" + moduleName;
        //Web文件输出路径
        String jspOutPath = path + "\\" + CodeResourceBundleUtil.getWebSourceFolder() + "\\jsp\\" + CodeResourceBundleUtil.getBuildPackage() + "\\" + moduleName;
        String jsOutPath = path + "\\" + CodeResourceBundleUtil.getWebSourceFolder() + "\\js\\" + CodeResourceBundleUtil.getBuildPackage() + "\\" + moduleName;
        //模版编译执行
        StringBuffer sb = new StringBuffer();
        //Model代码生成
        if (chckbxModel.isSelected()) {
            log.info("Model文件开始生成。。。");
            String fileName = FreemarkerUtil.compile(templatePath, "model.ftl", map, outPath + "/model/" + className + ".java");
            if (fileName == null || fileName.equals("")) {
                String msg = "Model文件生成失败。。。";
                sb.append(msg + "\n");
                log.info(msg);
            } else {
                String msg = fileName + "文件生成完毕。。。";
                sb.append(msg + "\n");
                log.info(msg);
            }
        }
        //Dao代码生成
        if (chckbxDao.isSelected()) {
            log.info("Dao文件开始生成。。。");
            String fileName = FreemarkerUtil.compile(templatePath, "dao.ftl", map, outPath + "/dao/" + className + "Dao.java");
            if (fileName == null || fileName.equals("")) {
                String msg = "Dao文件生成失败。。。";
                sb.append(msg + "\n");
                log.info(msg);
            } else {
                String msg = fileName + "文件生成完毕。。。";
                sb.append(msg + "\n");
                log.info(msg);
            }
        }
        //Service代码生成
        if (chckbxService.isSelected()) {
            log.info("Service文件开始生成。。。");
            String fileName = FreemarkerUtil.compile(templatePath, "service.ftl", map, outPath + "/service/" + className + "Service.java");
            if (fileName == null || fileName.equals("")) {
                String msg = "Service文件生成失败。。。";
                sb.append(msg + "\n");
                log.info(msg);
            } else {
                String msg = fileName + "文件生成完毕。。。";
                sb.append(msg + "\n");
                log.info(msg);
            }
            log.info("ServiceImpl文件开始生成。。。");
            String fileNameImpl = FreemarkerUtil.compile(templatePath, "serviceImpl.ftl", map, outPath + "/service/impl/" + className + "ServiceImpl.java");
            if (fileNameImpl == null || fileNameImpl.equals("")) {
                String msg = "ServiceImpl文件生成失败。。。";
                sb.append(msg + "\n");
                log.info(msg);
            } else {
                String msg = fileNameImpl + "文件生成完毕。。。";
                sb.append(msg + "\n");
                log.info(msg);
            }
        }
        //Controller代码生成
        if (chckbxController.isSelected()) {
            log.info("Controller文件开始生成。。。");
            String fileName = FreemarkerUtil.compile(templatePath, "controller.ftl", map, outPath + "/controller/" + className + "Controller.java");
            if (fileName == null || fileName.equals("")) {
                String msg = "Controller文件生成失败。。。";
                sb.append(msg + "\n");
                log.info(msg);
            } else {
                String msg = fileName + "文件生成完毕。。。";
                sb.append(msg + "\n");
                log.info(msg);
            }
        }
        //Jsp代码生成
        if (chckbxJsp.isSelected()) {
            log.info("Jsp文件开始生成。。。");
            String fileName = FreemarkerUtil.compile(templatePath, "jsp.ftl", map, jspOutPath + "\\" + className + ".jsp", true);
            if (fileName == null || fileName.equals("")) {
                String msg = "Jsp文件生成失败。。。";
                sb.append(msg + "\n");
                log.info(msg);
            } else {
                String msg = fileName + "文件生成完毕。。。";
                sb.append(msg + "\n");
                log.info(msg);
            }
        }
        //Js代码生成
        if (chckbxJsp.isSelected()) {
            log.info("Js文件开始生成。。。");
            String fileName = FreemarkerUtil.compile(templatePath, "js.ftl", map, jsOutPath + "\\" + className + ".js", true);
            if (fileName == null || fileName.equals("")) {
                String msg = "Js文件生成失败。。。";
                sb.append(msg + "\n");
                log.info(msg);
            } else {
                String msg = fileName + "文件生成完毕。。。";
                sb.append(msg + "\n");
                log.info(msg);
            }
        }
        JOptionPane.showMessageDialog(frame, sb.toString());
    }

    /**
     * 获取表的信息
     *
     * @param tableName
     * @return
     * @author alan
     * @date 2014-8-13 上午10:13:33
     */
    private Map<String, Object> getTableMessage(String tableName) {
        Map<String, Object> map = new HashMap<String, Object>();
        String[] filterFields = CodeResourceBundleUtil.getFilterFields().split(",");
        try {
            //获取表的信息
            ResultSet rss = dbmd.getColumns(null, null, tableName, null);
            List<Field> fields = new ArrayList<Field>();
            while (rss.next()) {
                //列名
                String columnName = rss.getString("COLUMN_NAME");
                if (ArrayUtils.contains(filterFields, columnName)) {
                    continue;
                }
                //列类型
                String columnType = rss.getString("TYPE_NAME");
                //列注释
                String columnRemark = rss.getString("REMARKS");
                //java字段名
                String fieldName = CamelCaseUtils.toCamelCase(columnName);
                //java方法名
                String methodName = CamelCaseUtils.toCapitalizeCamelCase(columnName);
                //判断备注是否为空，为空就用字段名
                if (columnRemark == null || "".equals(columnRemark)) {
                    columnRemark = fieldName;
                }
                //装配field对象
                Field field = new Field();
                field.setColumnName(columnName);
                field.setColumnType(columnType);
                field.setColumnRemark(columnRemark);
                field.setFieldName(fieldName);
                field.setFieldType(columnType);
                field.setMethodName(methodName);
                fields.add(field);
            }

            map.put("fields", fields);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
