package Frontend.GUI.DoiTra;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import Backend.BUS.DoiTraBUS;
import Backend.DTO.DoiTra;
import Frontend.Compoent.BaseThaoTacDialog;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class DoiTraFixDialog extends BaseThaoTacDialog {

    private JTextField txtMa, txtMaPX, txtMaKH, txtMaImei, txtNgayBan, txtHanCuoi, txtLyDo;
    private JButton btnChonPX, btnChonImei;
    private JSpinner jdNgayDoi;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Panel hiển thị thông tin sản phẩm
    private JLabel lblProductImage;
    private JTextField txtProductInfo, txtProductPrice, txtProductStatus;

    private DoiTraBUS doiTraBUS = new DoiTraBUS();

    public DoiTraFixDialog(DoiTra dt) {
        super("CẬP NHẬT PHIẾU ĐỔI TRẢ", 500, 600);

        txtMa.setText(dt.getMaDoiTra());
        txtMaPX.setText(dt.getMaPhieuXuat());
        txtMaKH.setText(dt.getMaKH());
        txtMaImei.setText(dt.getMaImei());
        txtLyDo.setText(dt.getLyDo());

        if (dt.getNgayDoiTra() != null) {
            Date date = Date.from(dt.getNgayDoiTra().atStartOfDay(ZoneId.systemDefault()).toInstant());
            ((SpinnerDateModel) jdNgayDoi.getModel()).setValue(date);
        }

        txtMa.setEditable(false);
        txtMa.setBackground(new Color(240, 240, 240));
        
        // Load thông tin sản phẩm hiện tại
        if (dt.getMaImei() != null && !dt.getMaImei().isEmpty()) {
            loadProductInfo(dt.getMaImei());
        }
    }

    @Override
    protected void initForm() {
        pnlContent.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 20", "[right]15[grow, fill]"));

        pnlContent.add(new JLabel("Mã phiếu đổi trả:"));
        txtMa = new JTextField();
        pnlContent.add(txtMa, "h 35!, wrap");

        pnlContent.add(new JLabel("Chọn Phiếu Xuất:"));
        txtMaPX = new JTextField();
        txtMaPX.setEditable(false);
        btnChonPX = new JButton("...");
        btnChonPX.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonPX.addActionListener(e -> openSelectPhieuXuat());
        pnlContent.add(txtMaPX, "split 2, growx, h 35!");
        pnlContent.add(btnChonPX, "w 40!, h 35!, wrap");

        pnlContent.add(new JLabel("Mã khách hàng:"));
        txtMaKH = new JTextField();
        txtMaKH.setEditable(false);
        txtMaKH.setBackground(new Color(240, 240, 240));
        pnlContent.add(txtMaKH, "h 35!, wrap");

        pnlContent.add(new JLabel("Ngày bán:"));
        txtNgayBan = new JTextField();
        txtNgayBan.setEditable(false);
        txtNgayBan.setBackground(new Color(240, 240, 240));
        pnlContent.add(txtNgayBan, "h 35!, wrap");

        pnlContent.add(new JLabel("Hạn cuối đổi trả:"));
        txtHanCuoi = new JTextField();
        txtHanCuoi.setEditable(false);
        txtHanCuoi.setBackground(new Color(240, 240, 240));
        pnlContent.add(txtHanCuoi, "h 35!, wrap");

        pnlContent.add(new JLabel("Mã IMEI máy trả:"));
        txtMaImei = new JTextField();
        txtMaImei.setEditable(false);
        btnChonImei = new JButton("...");
        btnChonImei.addActionListener(e -> openSelectImei());
        pnlContent.add(txtMaImei, "split 2, growx, h 35!");
        pnlContent.add(btnChonImei, "w 40!, h 35!, wrap");

        pnlContent.add(new JLabel("Ngày đổi trả:"));
        SpinnerDateModel modelNgayDoi = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        jdNgayDoi = new JSpinner(modelNgayDoi);
        jdNgayDoi.setEditor(new JSpinner.DateEditor(jdNgayDoi, "dd/MM/yyyy"));
        pnlContent.add(jdNgayDoi, "h 35!, wrap");

        pnlContent.add(new JLabel("Lý do: "));
        txtLyDo = new JTextField();
        pnlContent.add(txtLyDo, "h 35!, wrap");
        
        // Panel hiển thị thông tin sản phẩm
        // pnlContent.add(new JLabel("Thông tin sản phẩm:"), "newline");
        
        // JPanel pnlProductInfo = new JPanel(new MigLayout("wrap 1, fill, insets 10", "[center]"));
        // pnlProductInfo.setBackground(new Color(248, 249, 250));
        // pnlProductInfo.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        // lblProductImage = new JLabel();
        // lblProductImage.setHorizontalAlignment(JLabel.CENTER);
        // lblProductImage.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        // pnlProductInfo.add(lblProductImage, "w 220!, h 180!");
        
        // txtProductInfo = new JTextField();
        // txtProductInfo.setEditable(false);
        // txtProductInfo.setBackground(new Color(240, 240, 240));
        // pnlProductInfo.add(txtProductInfo, "growx, h 35!");
        
        // txtProductPrice = new JTextField();
        // txtProductPrice.setEditable(false);
        // txtProductPrice.setBackground(new Color(240, 240, 240));
        // pnlProductInfo.add(txtProductPrice, "growx, h 35!");
        
        // txtProductStatus = new JTextField();
        // txtProductStatus.setEditable(false);
        // txtProductStatus.setBackground(new Color(240, 240, 240));
        // pnlProductInfo.add(txtProductStatus, "growx, h 35!");
        
        // pnlContent.add(pnlProductInfo, "span 2, grow");
    }

    private void openSelectPhieuXuat() {
        SelectPhieuXuatDialog dialog = new SelectPhieuXuatDialog();
        dialog.setVisible(true);
        Backend.DTO.PhieuXuat px = dialog.getSelectedPX();
        if (px != null) {
            txtMaPX.setText(px.getMaPhieuXuat());
            txtMaKH.setText(px.getMaKH());
            if (px.getNgayXuat() != null) {
                LocalDate ngayBan = px.getNgayXuat().toLocalDateTime().toLocalDate();
                txtNgayBan.setText(ngayBan.format(dtf));
                txtHanCuoi.setText(ngayBan.plusDays(30).format(dtf));
            }
            txtMaImei.setText(""); 
        }
    }

    private void openSelectImei() {
        String maPX = txtMaPX.getText();
        if(maPX.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Phiếu xuất trước!");
            return;
        }
        SelectImeiDialog dialog = new SelectImeiDialog(maPX);
        dialog.setVisible(true);
        if (dialog.getSelectedImei() != null) {
            String imei = dialog.getSelectedImei();
            txtMaImei.setText(imei);
            
            // Load thông tin sản phẩm từ IMEI
            loadProductInfo(imei);
        }
    }
    
    private void loadProductInfo(String imei) {
        try {
            HashMap<String, String> productInfo = doiTraBUS.getProductInfoByImei(imei);
            
            if (productInfo != null && !productInfo.isEmpty()) {
                // Load hình ảnh
                String hinhAnh = productInfo.get("hinhAnh");
                if (hinhAnh != null && !hinhAnh.isEmpty()) {
                    loadProductImage(hinhAnh);
                } else {
                    lblProductImage.setIcon(null);
                    lblProductImage.setText("Không có ảnh");
                }
                
                // Load thông tin sản phẩm
                String tenSP = productInfo.get("tenSP");
                String mauSac = productInfo.get("mauSac");
                String congSuat = productInfo.get("congSuat");
                String productText = "🔹 " + tenSP + "\n📦 " + mauSac + " | " + congSuat;
                txtProductInfo.setText(productText);
                
                // Load giá
                String giaBan = productInfo.get("giaBan");
                try {
                    double gia = Double.parseDouble(giaBan);
                    txtProductPrice.setText("💰 Giá: " + String.format("%,.0f VNĐ", gia));
                } catch (Exception ex) {
                    txtProductPrice.setText("💰 Giá: N/A");
                }
                
                // Load tình trạng
                String tinhTrang = productInfo.get("tinhTrang");
                txtProductStatus.setText("📊 Tình trạng: " + tinhTrang);
            }
        } catch (Exception ex) {
            System.err.println("Lỗi load thông tin sản phẩm: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void loadProductImage(String imgName) {
        try {
            java.net.URL imgURL = getClass().getClassLoader().getResource("images/product/" + imgName);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image scaled = icon.getImage().getScaledInstance(220, 180, Image.SCALE_SMOOTH);
                lblProductImage.setIcon(new ImageIcon(scaled));
                lblProductImage.setText("");
            } else {
                lblProductImage.setIcon(null);
                lblProductImage.setText("Không tìm thấy ảnh");
            }
        } catch (Exception e) {
            lblProductImage.setIcon(null);
            lblProductImage.setText("Lỗi tải ảnh");
            e.printStackTrace();
        }
    }

    @Override
    protected void logicXacNhan() {
        if (txtMaPX.getText().isEmpty() || txtMaImei.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Phiếu xuất và IMEI!");
            return;
        }

        if (txtLyDo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lý do!");
            return;
        }

        Date dateDoi = (Date) ((SpinnerDateModel) jdNgayDoi.getModel()).getValue();
        if (dateDoi == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
            return;
        }

        LocalDate ngayDoiTra = dateDoi.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // DoiTra dt = new DoiTra();
        // dt.setMaDoiTra(txtMa.getText());
        // dt.setMaKH(txtMaKH.getText());
        // dt.setMaPhieuXuat(txtMaPX.getText());
        // dt.setMaImei(txtMaImei.getText());
        // dt.setNgayDoiTra(ngayDoiTra);
        // dt.setLyDo(txtLyDo.getText());

        DoiTra dt = new DoiTra();
        dt.setMaDoiTra(txtMa.getText());
        dt.setNgayDoiTra(((Date) ((SpinnerDateModel) jdNgayDoi.getModel()).getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        dt.setLyDo(txtLyDo.getText());

        String res = doiTraBUS.update(dt);

        if (res.equals("OK")) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, res, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}