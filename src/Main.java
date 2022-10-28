import caixa_eletronico.*;
import ui.*;

public class Main {
    public static void main(String[] args) {
        ICaixaEletronico cx = new CaixaEletronico();
        JFrameGUI janela = new JFrameGUI(cx);
        janela.setVisible(true);
    }
}
