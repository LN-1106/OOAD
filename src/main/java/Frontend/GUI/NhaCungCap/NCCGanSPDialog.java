package Frontend.GUI.NhaCungCap;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Backend.BUS.NhaCungCapBUS;
import Backend.BUS.SanPhamBUS;
import Backend.DTO.SanPham;
import Frontend.Compoent.Table;
import Frontend.Compoent.CustomButton;
import java.awt.*;
import javax.swing.*;

public class NCCGanSPDialog extends JDialog{
    private JTable tblSP;
    private DefaultTableModel model;
    private String maNCC;
    private NhaCungCapBUS nccBUS = new NhaCungCapBUS();
    private SanPhamBUS spBUS = new SanPhamBUS();

    public NCCGanSPDialog(JFrame parent, String maNCC, String tenNCC) {
        super(parent, "Gán sản phẩm cho: " + tenNCC, true);
        this.maNCC = maNCC;
        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] cols = {"Chọn", "Mã SP", "Tên Sản Phẩm"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }
        };

        tblSP = new Table();
        tblSP.setModel(model);
        loadData();

        add(new JScrollPane(tblSP), BorderLayout.CENTER);

        CustomButton btnSave = new CustomButton("LƯU THÔNG TIN", Color.LIGHT_GRAY);
        btnSave.addActionListener(e -> {
            ArrayList<String> selected = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                if ((Boolean) model.getValueAt(i, 0)) {
                    selected.add(model.getValueAt(i, 1).toString());
                }
            }
            if (nccBUS.saveSanPhamCungCap(maNCC, selected)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                dispose();
            }
        });
        add(btnSave, BorderLayout.SOUTH);
    }

    private void loadData() {
        // Lấy tất cả sản phẩm trong kho
        ArrayList<SanPham> allSP = spBUS.getAll();
        // Lấy danh sách mã SP mà NCC này đang có
        ArrayList<String> currentSP = nccBUS.getMaSPByNCC(maNCC);

        for(SanPham sp : allSP) {
            model.addRow(new Object[]{
                currentSP.contains(sp.getMaSP()),
                sp.getMaSP(),
                sp.getTenSP()
            });
        }
    }
}
