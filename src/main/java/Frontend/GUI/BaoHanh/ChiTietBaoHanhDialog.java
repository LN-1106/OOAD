package Frontend.GUI.BaoHanh;

import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import Backend.DTO.BaoHanh;
import Backend.DTO.ChiTietBaoHanh;
import Backend.BUS.BaoHanhBUS;
import Backend.BUS.ChiTietBaoHanhBUS;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import net.miginfocom.swing.MigLayout;

public class ChiTietBaoHanhDialog extends JDialog {
    private String maBH;
    private JTable tblDetails;
    private DefaultTableModel model;
    private ChiTietBaoHanhBUS chiTietBaoHanhBUS;
    private BaoHanhBUS baoHanhBUS = new BaoHanhBUS();

    public ChiTietBaoHanhDialog(JFrame parent, String maBH) {
        super(parent, "Chi tiết bảo hành: " + maBH, true);
        this.maBH = maBH;
        this.chiTietBaoHanhBUS = new ChiTietBaoHanhBUS();
        setSize(850, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlHeader.setBackground(Theme.PRIMARY_COLOR);
        JLabel lblTitle = new JLabel("LỊCH SỬ SỬA CHỮA BẢO HÀNH");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlMain = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[grow]"));
        pnlMain.setBackground(Color.WHITE);

        String[] columns = { "STT", "Mã CTBH", "Mã BH", "Tên sản phẩm", "Nội dung", "Tình trạng" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tblDetails = new Table();
        tblDetails.setModel(model);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblDetails.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tblDetails);
        pnlMain.add(scrollPane, "grow");

        add(pnlMain, BorderLayout.CENTER);

        tblDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblDetails.getSelectedRow();
                    if (row == -1)
                        return;

                    String tinhTrangHienTai = model.getValueAt(row, 5).toString();

                    if (!tinhTrangHienTai.equalsIgnoreCase("Đang sửa chữa")
                            && !tinhTrangHienTai.equalsIgnoreCase("Còn bảo hành")) {
                        JOptionPane.showMessageDialog(ChiTietBaoHanhDialog.this,
                                "Chỉ có thể hoàn thành khi tình trạng là 'Đang sửa chữa'!",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(ChiTietBaoHanhDialog.this,
                            "Xác nhận chuyển tình trạng sang 'Hoàn thành'?",
                            "Xác nhận", JOptionPane.YES_NO_OPTION);

                    if (confirm != JOptionPane.YES_OPTION)
                        return;

                    String maCTBH = model.getValueAt(row, 1).toString();

                    ChiTietBaoHanh ctbh = new ChiTietBaoHanh();
                    ctbh.setMaCTBH(maCTBH);
                    ctbh.setNoiDung(model.getValueAt(row, 4).toString());
                    ctbh.setTinhTrang("Hoàn thành");

                    if (chiTietBaoHanhBUS.update(ctbh)) {
                        model.setValueAt("Hoàn thành", row, 5);
                        JOptionPane.showMessageDialog(ChiTietBaoHanhDialog.this,
                                "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(ChiTietBaoHanhDialog.this,
                                "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        ArrayList<ChiTietBaoHanh> listChiTietBaoHanh = chiTietBaoHanhBUS.getAllChiTietBaoHanh(this.maBH);

        int STT = 1;
        for (ChiTietBaoHanh ctbh : listChiTietBaoHanh) {
            Object[] row = {
                    STT++,
                    ctbh.getMaCTBH(),
                    ctbh.getMaBH(),
                    ctbh.getTenSP(),
                    ctbh.getNoiDung(),
                    ctbh.getTinhTrang()
            };
            model.addRow(row);
        }
    }

}