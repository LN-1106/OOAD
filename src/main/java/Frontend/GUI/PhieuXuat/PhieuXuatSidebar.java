package Frontend.GUI.PhieuXuat;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class PhieuXuatSidebar extends JPanel {
    private JSpinner dateFrom;
    private JSpinner dateTo;
    private JTextField txtMinPrice, txtMaxPrice;
    private JComboBox<String> cbxNhanVien;

    private PhieuXuatTable tblPhieuXuat;

    public PhieuXuatSidebar(PhieuXuatTable tblPhieuXuat) {
        this.tblPhieuXuat = tblPhieuXuat;
        setLayout(new MigLayout("wrap 1, fillx, insets 20", "[fill]"));
        setPreferredSize(new Dimension(280, 0));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        // initNhanVienXuat();
        initDate();
        initPrice();
        initResetButton();
    }

    // private void initNhanVienXuat() {
    // add(new JLabel("Nhân viên xuất"), "gaptop 10");
    // cbxNhanVien = new JComboBox<>(new String[] { "Tất cả", "Phúc Trương", "Văn
    // Nam" });
    // cbxNhanVien.addActionListener(e -> { onFilterChange(); });
    // add(cbxNhanVien, "h 35!");
    // }

    private void onFilterChange() {
        String minPriceClean = txtMinPrice.getText().replaceAll("[^0-9]", "");
        String maxPriceClean = txtMaxPrice.getText().replaceAll("[^0-9]", "");

        double min = 0;
        try {
            if (!minPriceClean.isEmpty())
                min = Double.parseDouble(minPriceClean);
        } catch (Exception e) {
            min = 0;
        }

        double max = Double.MAX_VALUE;
        try {
            if (!maxPriceClean.isEmpty())
                max = Double.parseDouble(maxPriceClean);
        } catch (Exception e) {
            max = Double.MAX_VALUE;
        }

        Date start = (Date) ((SpinnerDateModel) dateFrom.getModel()).getValue();
        Date end = (Date) ((SpinnerDateModel) dateTo.getModel()).getValue();

        String nv = "Tất cả";

        if (tblPhieuXuat != null) {
            tblPhieuXuat.filterData(start, end, nv, min, max);
        }
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
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                onFilterChange();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                onFilterChange();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                onFilterChange();
            }
        };

        txtMinPrice.getDocument().addDocumentListener(priceListener);
        txtMaxPrice.getDocument().addDocumentListener(priceListener);
    }

    private void initResetButton() {
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