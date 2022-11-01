package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Font;

import javax.swing.JButton;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import caixa_eletronico.ICaixaEletronico;
import caixa_eletronico.CaixaEletronico;

public class JFrameGUI extends JFrame {
	private static final String MSG_ERRO_GENERICO = "Erro. Não é possível continuar a operação\n";
	private JPanel contentPane;
	private ICaixaEletronico cx;
	private StringBuilder relatorio;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameGUI frame = new JFrameGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JFrameGUI() {
		this(new CaixaEletronico());
	}
	
	
	public JFrameGUI(ICaixaEletronico cx) {
		this.cx = cx;
		this.relatorio = new StringBuilder();
		postTimestamp();
		postSaldo();
		
		setTitle("Caixa eletronico");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 280, 321);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblModuloCliente = new JLabel("Modulo do Cliente:");
		lblModuloCliente.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblModuloCliente = new GridBagConstraints();
		gbc_lblModuloCliente.anchor = GridBagConstraints.WEST;
		gbc_lblModuloCliente.insets = new Insets(0, 0, 5, 0);
		gbc_lblModuloCliente.gridx = 0;
		gbc_lblModuloCliente.gridy = 0;
		contentPane.add(lblModuloCliente, gbc_lblModuloCliente);
		
		JButton btnSaque = new JButton("Efetuar Saque");
		btnSaque.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_btnSaque = new GridBagConstraints();
		gbc_btnSaque.insets = new Insets(0, 0, 5, 0);
		gbc_btnSaque.gridx = 0;
		gbc_btnSaque.gridy = 1;
		gbc_btnSaque.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(btnSaque, gbc_btnSaque);
		
		JLabel lblModuloAdministrador = new JLabel("Modulo do Administrador:");
		lblModuloAdministrador.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblModuloAdministrador = new GridBagConstraints();
		gbc_lblModuloAdministrador.anchor = GridBagConstraints.WEST;
		gbc_lblModuloAdministrador.insets = new Insets(0, 0, 5, 0);
		gbc_lblModuloAdministrador.gridx = 0;
		gbc_lblModuloAdministrador.gridy = 2;
		contentPane.add(lblModuloAdministrador, gbc_lblModuloAdministrador);
		
		JButton btnRelatorio = new JButton("Relatorio de Cedulas");
		btnRelatorio.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_btnRelatorio = new GridBagConstraints();
		gbc_btnRelatorio.insets = new Insets(0, 0, 5, 0);
		gbc_btnRelatorio.gridx = 0;
		gbc_btnRelatorio.gridy = 3;
		gbc_btnRelatorio.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(btnRelatorio, gbc_btnRelatorio);
		
		JButton btnValorTotal = new JButton("Valor total disponivel");
		btnValorTotal.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_btnValorTotal = new GridBagConstraints();
		gbc_btnValorTotal.insets = new Insets(0, 0, 5, 0);
		gbc_btnValorTotal.gridx = 0;
		gbc_btnValorTotal.gridy = 4;
		gbc_btnValorTotal.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(btnValorTotal, gbc_btnValorTotal);
		
		JButton btnReposicao = new JButton("Reposicao de Cedulas");
		btnReposicao.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_btnReposicao = new GridBagConstraints();
		gbc_btnReposicao.insets = new Insets(0, 0, 5, 0);
		gbc_btnReposicao.gridx = 0;
		gbc_btnReposicao.gridy = 5;
		gbc_btnReposicao.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(btnReposicao, gbc_btnReposicao);
		
		JButton btnCota = new JButton("Cota Minima");
		btnCota.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_btnCota = new GridBagConstraints();
		gbc_btnCota.insets = new Insets(0, 0, 5, 0);
		gbc_btnCota.gridx = 0;
		gbc_btnCota.gridy = 6;
		gbc_btnCota.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(btnCota, gbc_btnCota);
		
		JLabel lblModuloAmbos = new JLabel("Modulo de Ambos:");
		lblModuloAmbos.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblModuloAmbos = new GridBagConstraints();
		gbc_lblModuloAmbos.anchor = GridBagConstraints.WEST;
		gbc_lblModuloAmbos.insets = new Insets(0, 0, 5, 0);
		gbc_lblModuloAmbos.gridx = 0;
		gbc_lblModuloAmbos.gridy = 7;
		contentPane.add(lblModuloAmbos, gbc_lblModuloAmbos);
		
		JButton btnSair = new JButton("Sair");
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_btnSair = new GridBagConstraints();
		gbc_btnSair.gridx = 0;
		gbc_btnSair.gridy = 8;
		gbc_btnSair.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(btnSair, gbc_btnSair);
		
		btnSaque.addActionListener((l) -> { sacar(); });
		
		btnRelatorio.addActionListener((l) -> {
			JOptionPane.showMessageDialog(this, cx.pegaRelatorioCedulas(), 
					"Relatorio de cédulas", JOptionPane.INFORMATION_MESSAGE);
		});
		
		btnValorTotal.addActionListener((l) -> {
			JOptionPane.showMessageDialog(this, cx.pegaValorTotalDisponivel(), 
					"Valor total disponivel", JOptionPane.INFORMATION_MESSAGE);
		});
		
		btnReposicao.addActionListener((l) -> { reporCedulas(); });
		
		btnCota.addActionListener((l) -> { registrarCota(); });
		
		
		btnSair.addActionListener((l) -> {
			JPanel textPanel = new JPanel();
			textPanel.add( new TextArea( getAtmStatement() ) );
			JOptionPane.showMessageDialog(this, textPanel, "Relatorio da sessão", JOptionPane.INFORMATION_MESSAGE);
			this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		});
		
		
	}
	
	private String getTimestamp() {
		return new SimpleDateFormat("d/M HH:mm:ss\n").format( Calendar.getInstance().getTime());
	}
	
	private void postTimestamp() {
		relatorio.append(getTimestamp());
	}
	
	private void postSaldo() {
		relatorio.append("Saldo: %s\n".formatted(cx.pegaValorTotalDisponivel().strip()));
	}
	
	private void sacar() {
		final String TITULO = "Saque";
		final String SALDO_INICIAL = cx.pegaValorTotalDisponivel();
		try {
			final String STR_VALUE = (String) JOptionPane.showInputDialog(this, "Valor do saque", TITULO, 
					JOptionPane.QUESTION_MESSAGE, null, null, 0);
			if(STR_VALUE == null) {
				return;
			}
			final Integer VALUE = Integer.valueOf(STR_VALUE);
			JOptionPane.showMessageDialog(this, cx.sacar(VALUE), TITULO, JOptionPane.INFORMATION_MESSAGE);
			final String SALDO_FINAL = cx.pegaValorTotalDisponivel();
			
			if(!SALDO_FINAL.equals(SALDO_INICIAL)) {
				postTimestamp();
				relatorio.append(String.format(Locale.ENGLISH, "Saque: R$ %.2f\n", VALUE.floatValue()));
				postSaldo();
			}
		} catch (NumberFormatException e) { 
			JOptionPane.showMessageDialog(this, MSG_ERRO_GENERICO, TITULO, JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void reporCedulas() {
		final String TITULO = "Reposição de cédulas";
		final String SALDO_INICIAL = cx.pegaValorTotalDisponivel();
		try {				
			final String userV =  (String) JOptionPane.showInputDialog(this, "Valor da cédula", TITULO, 
					JOptionPane.QUESTION_MESSAGE, null, null, 0);
			if(userV == null) { 
				return; 
			}
			final Integer v = Integer.valueOf(userV);
			final String userQ = (String) JOptionPane.showInputDialog(this, "Quantidade", TITULO, 
					JOptionPane.QUESTION_MESSAGE, null, null, 0);
			if(userQ == null) { 
				return; 
			}
			final Integer q = Integer.valueOf(userQ);
			JOptionPane.showMessageDialog(this, cx.reposicaoCedulas(v, q), TITULO, 
					JOptionPane.INFORMATION_MESSAGE);
			final String SALDO_FINAL = cx.pegaValorTotalDisponivel();
			if(!SALDO_FINAL.equals(SALDO_INICIAL)) {
				postTimestamp();
				relatorio.append(String.format(Locale.ENGLISH, "Reposicao: R$ %.2f\n", q.floatValue() * v.floatValue()));
				postSaldo();
			}
		} catch (NumberFormatException e) { 
			JOptionPane.showMessageDialog(this, MSG_ERRO_GENERICO, TITULO, JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void registrarCota() {
		final String titulo = "Cota Minima";
		try {				
			final String userMin =  (String) JOptionPane.showInputDialog(this, "Valor da cota minima", titulo, 
					JOptionPane.QUESTION_MESSAGE, null, null, 0);
			if(userMin == null) { 
				return; 
			}
			final Integer min = Integer.valueOf(userMin);
			JOptionPane.showMessageDialog(this, cx.armazenaCotaMinima(min), titulo, JOptionPane.INFORMATION_MESSAGE);
			return;
		} catch (NumberFormatException e) { 
			JOptionPane.showMessageDialog(this, MSG_ERRO_GENERICO, titulo, JOptionPane.WARNING_MESSAGE);
		}		
	}
	
	private String getAtmStatement() {
		return relatorio.toString();
	}

}
