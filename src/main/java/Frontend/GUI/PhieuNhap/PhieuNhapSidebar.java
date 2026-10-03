package Frontend.GUI.PhieuNhap;

import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Theme;
import Frontend.GUI.Nhaphang.NhapHangTable;

import Backend.BUS.NhaCungCapBUS;
import Backend.DTO.NhaCungCap;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class PhieuNhapSidebar extends JPanel{
    private JSpinner dateFrom;
    private JSpinner dateTo;
    private JTextField txtMinPrice, txtMaxPrice;
    private JComboBox<String> cbxNhanVien;
    private JComboBox<NhaCungCap> cbxNhaCungCap;

    private PhieuNhapTable table;

    public PhieuNhapSidebar(PhieuNhapTable table) {
        this.table = table;
        setLayout(new MigLayout("wrap 1, fillx, insets 20", "[fill]"));
        setPreferredSize(new Dimension(280, 0));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        // initNhanVienNhap();
        // initNhaCungCap();
        initDate();
        initPrice();
        initResetButton();
    }

    // private void initNhanVienNhap() {
    //     add(new JLabel("Nhân viên nhập"), "gaptop 10");
    //     cbxNhanVien = new JComboBox<>(new String[] { "Tất cả", "Phuc truong", "Van Nam" });
    //     add(cbxNhanVien, "h 35!");
    // }

    // private void initNhaCungCap() {
    //     add(new JLabel("Nhà cung cấp"), "gaptop 10");
    //     cbxNhaCungCap = new JComboBox<>();

    //     NhaCungCapBUS nccBUS = new NhaCungCapBUS();
    //     ArrayList<NhaCungCap> list = nccBUS.getAllNhaCungCap();

    //     DefaultComboBoxModel<NhaCungCap> model = new DefaultComboBoxModel<>();
    //     model.addElement(new NhaCungCap("All", "Tất cả", " ", ""));

    //     for(NhaCungCap ncc : list) {
    //         model.addElement(ncc);
    //     }
    //     cbxNhaCungCap.setModel(model);

    //     cbxNhaCungCap.addActionListener(e -> { onFilterChange(); });

    //     add(cbxNhaCungCap, "h 35!");
    // }

    private void onFilterChange() {
        Date start = getStartOfDay((Date) ((SpinnerDateModel) dateFrom.getModel()).getValue());
        Date end = getEndOfDay((Date) ((SpinnerDateModel) dateTo.getModel()).getValue());
        // NhaCungCap ncc = (NhaCungCap) cbxNhaCungCap.getSelectedItem();
        // String maNCC = (ncc != null) ? ncc.getMaNCC() : "All";
        table.filter(/*maNCC,*/ start, end, txtMinPrice.getText(), txtMaxPrice.getText());
    }

    private Date getStartOfDay(Date date) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date getEndOfDay(Date date) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    private void initDate() {
        add(new JLabel("Từ ngày:"), "gaptop 10");
        Calendar calFrom = Calendar.getInstance();
        calFrom.set(2000, 0, 1);
        SpinnerDateModel modelFrom = new SpinnerDateModel(calFrom.getTime(), null, null, Calendar.DAY_OF_MONTH);
        dateFrom = new JSpinner(modelFrom);
        dateFrom.setEditor(new JSpinner.DateEditor(dateFrom, "dd/MM/yyyy"));
        add(dateFrom, "h 35!");

        add(new JLabel("Đến ngày:"), "gaptop 10");
        SpinnerDateModel modelTo = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        dateTo = new JSpinner(modelTo);
        dateTo.setEditor(new JSpinner.DateEditor(dateTo, "dd/MM/yyyy"));
        add(dateTo, "h 35!");

        ChangeListener changeListener = e -> onFilterChange();
        modelFrom.addChangeListener(changeListener);
        modelTo.addChangeListener(changeListener);
    }

    private void initPrice() {
        add(new JLabel("Số tiền từ (VNĐ):"), "gaptop 10");
        txtMinPrice = new JTextField();
        add(txtMinPrice, "h 35!");
        add(new JLabel("Đến số tiền (VNĐ):"), "gaptop 10");
        txtMaxPrice = new JTextField();
        add(txtMaxPrice, "h 35!");

        javax.swing.event.DocumentListener priceListener = new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) { onFilterChange(); }
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) { onFilterChange(); }
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) { onFilterChange(); }
        };

        txtMinPrice.getDocument().addDocumentListener(priceListener);
        txtMaxPrice.getDocument().addDocumentListener(priceListener);
    }

    private void initResetButton() {
        // add(new JLabel(""), "gaptop 20, push"); // khoảng trống để đẩy nút xuống dưới
        // JButton btnXacNhan = new JButton("Đặt lại mặc định");
        // btnXacNhan.setBackground(new Color(230, 126, 34));
        // btnXacNhan.setForeground(Color.WHITE);
        // btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        // btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        CustomButton btnXacNhan = new CustomButton("MẶC ĐỊNH", Theme.ACCENT_COLOR);
        
        btnXacNhan.addActionListener(e -> resetFilters());
        
        add(btnXacNhan, "gaptop 5, growx, h 40!, pushy, aligny bottom");
    }

    private void resetFilters() {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 0, 1);
        ((SpinnerDateModel) dateFrom.getModel()).setValue(cal.getTime());
        
        ((SpinnerDateModel) dateTo.getModel()).setValue(new Date());
        
        txtMinPrice.setText("");
        txtMaxPrice.setText("");
        
        onFilterChange();
    }
}
