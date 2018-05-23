package pacote;

/**
 * @version	versão
 * @param	parâmetro do método
 * @return	retorno do método
 * @exception	exceção
 * @see	veja também
 *
 * <p>The MIT License</p> <p>Copyright: Copyright (C) 2011 Raul Lopes</p>
 *
 * @author	<p>Raul Lopes The author may be contacted at:
 * raul.lagoa@gmail.com</p>
 */

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author PC
 */
public class ExecucaoAutomatica {

    public static TrayIcon trayIcon = null; // declarando uma constante do tipo TrayIcon
    // Criando e iniciando a thread para executar a sincronizacao
    Thread thread = new ThreadSincroniza();
    private boolean ConfgAberta = false;
    //private static Properties arquivoConfig;
    private boolean executa = false;
    private CheckboxMenuItem checkPausar;
    private int intervaloPausaMinutos;
    Image image = null;
    Image imagePausa = null;

    public void setConfgAberta(boolean ConfgAberta) {
        this.ConfgAberta = ConfgAberta;
    }

    public static void main(String[] args) {
        new ExecucaoAutomatica();
    }

    public ExecucaoAutomatica() {

        intervaloPausaMinutos = 10; //10 minutos

        //testar se o recurso é suportado
        if (SystemTray.isSupported()) {

            //declarando uma variavel  do tipo SystemTray
            SystemTray tray = SystemTray.getSystemTray();

            //declarando uma variavel  do tipo Image que contera a imagem tray.gif
            //Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("IconSincro.png"));           

            try {
                image = Toolkit.getDefaultToolkit().getImage(Constantes.SH_ICONE);
                imagePausa = Toolkit.getDefaultToolkit().getImage(Constantes.SH_ICONE_PAUSA);
            } catch (Exception e) {
                trayIcon.setImage(imagePausa);
                JOptionPane.showMessageDialog(null, "Ops! arquivo de configuração não foi encontrado.", Constantes.AVISO_SISTEMA, JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }

            //Criamos um listener para escutar os eventos do mouse
            MouseListener mouseListener = new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }
            };

            //===================================================================================================                     
            // Criamos um ActionListener para a exibir uma mensagem na tela ao clicarmos no "item do menu sobre".
            //---------------------------------------------------------------------------------------------------                    
            ActionListener mostrarSobreListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    sobreSistema();
                }
            };

            ActionListener configuracoesListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    //open some form here
                    
                }
            };

            ItemListener pausarSincronizacaoListener = new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {

                    if (checkPausar.getState()) {

                        //capturar o tempo de pausa

                        trayIcon.setImage(imagePausa);
                        System.out.println("pausou a execução do sistema.");
                        intervaloPausaMinutos = 2;
                        //thread.interrupt(); //Finalizamos as threads. O método interrupt não para a thread, a menos que ela seja programada para reagir a ele. Ele fará com que waits gerem interruptedException e o método Thread.isInterrupted() (caso o interrupt seja lançado fora de um wait) retorne true. 

                    } else {

                        //executa = true;
                        intervaloPausaMinutos = 1;

                        trayIcon.setImage(image);
                        System.out.println("re-iniciou execução do sistema");
                    }
                }
            };

            // Criei um ActionListener para a ação de encerramento do programa.
            ActionListener sairListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    //Confirmação ao solicitar a saida do sistema

                    String[] opcoes = {"Sim", "Não"};
                    int escolha = JOptionPane.showOptionDialog(null, "Deseja sair do sistema ?",
                            Constantes.PERGUNTA_SISTEMA,
                            JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null, opcoes, null);

                    if (escolha == 0) {
                        thread.currentThread().interrupt(); // termina a Thread quando sair de uma transacao
                        System.exit(0);
                    }
                }
            };

            //===========================================================================                     
            //Criando um objeto PopupMenu
            //---------------------------------------------------------------------------                    
            //MenuItem configMenu = new MenuItem("Configurações");
            //configMenu.addActionListener(Configuracoes);
            PopupMenu popup = new PopupMenu("Menu do Sistema");

            //criando itens do menu
            MenuItem mostrarSobre = new MenuItem("Sobre");
            //na linha a seguir associamos os objetos aos eventos
            mostrarSobre.addActionListener(mostrarSobreListener);
            //Adicionando itens ao PopupMenu
            popup.add(mostrarSobre);
            //adiconando um separador
            popup.addSeparator();

            //opcao de pausar
            checkPausar = new CheckboxMenuItem("Pausar ?");
            checkPausar.addItemListener(pausarSincronizacaoListener);
            checkPausar.setState(executa);
            popup.add(checkPausar);
            popup.addSeparator();

            //Criando um submenu -----------------------------------------------
            PopupMenu subPopupOpcoes = new PopupMenu("Opções");
            MenuItem mostrConfiguracao = new MenuItem("Configurações");
            mostrConfiguracao.addActionListener(configuracoesListener);
            //MenuItem mostramsg2 = new MenuItem("Item2");
            //MenuItem mostramsg3 = new MenuItem("Item3");
            subPopupOpcoes.add(mostrConfiguracao);
            //popupOpcoes.add(mostramsg1);
            //popupOpcoes.add(mostramsg3);

            //adicionando submenu popup ao menu principal
            popup.add(subPopupOpcoes);
            //popup.add(configMenu);
            popup.addSeparator();

            //opcao sair -------------------------------------------------------
            MenuItem menuSair = new MenuItem("Sair");
            menuSair.addActionListener(sairListener);
            popup.add(menuSair);
            //------------------------------------------------------------------
            //Fim da criacao do objeto PopupMenu
            //==================================================================


            //==================================================================
            //criando um objeto do tipo TrayIcon
            //------------------------------------------------------------------
            trayIcon = new TrayIcon(image, "Sincronus - Aplicativo Sicronizador by Raul Lopes " + dataAtual(5), popup);

            //Na linha a seguir a imagem a ser utilizada como icone sera redimensionada
            trayIcon.setImageAutoSize(true);

            //Seguida adicionamos os actions listeners
            trayIcon.addMouseListener(mouseListener);

            try {

                tray.add(trayIcon);

            } catch (AWTException e) {
                trayIcon.setImage(imagePausa);
                JOptionPane.showMessageDialog(null, "Ops! TrayIcon não será adicionado.", Constantes.AVISO_SISTEMA, JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            //---------------------------------------------------------------------------                    
            //Fim da criacao do objeto do tipo TrayIcon
            //===========================================================================                                 
            thread.start();

        } else {

            //Caso o item  System Tray não for suportado
            JOptionPane.showMessageDialog(null, "Ops! Recurso incompatível para o seu sistema operacional.", Constantes.AVISO_SISTEMA, JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    //===========================================================================                     
    private void sobreSistema() {

        //---
    }

    private void executarSincronizacao() throws SQLException, ClassNotFoundException, IOException, Exception {

        synchronized (this) { //garantir que irá rodar apenas uma trhead por vez

            System.out.println("Iniciou...");

        } //--fim 1ºsincronizeded
    }
   public static String dataAtual(int modo) {

        Date date = new Date();
        DateFormat dateFormat = null;


        if (modo == 0) {
            dateFormat = new SimpleDateFormat("dd/MM/yy");
        }

        if (modo == 1) {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        }

        if (modo == 2) {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        }

        if (modo == 3) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }

        if (modo == 4) {
            dateFormat = new SimpleDateFormat("yyyyMMdd");
        }
        if (modo == 5) {
            dateFormat = new SimpleDateFormat("yyyy");
        }
        
        return dateFormat.format(date).trim();

    }
    //=====================================
    class ThreadSincroniza extends Thread {

        @Override
        public void run() {

            while (!Thread.interrupted()) {
                try { //executa

                    //Este método(run()) é chamado quando a thread é iniciada

                    executarSincronizacao();

                    //Finalizando Threads
                    //Uma Thread é finalizada quando acabar a execução do seu método run, 
                    //e então ela vai para o estado Morta, onde o Sistema de Execução Java poderá liberar seus recursos e eliminá-la.

                    ThreadSincroniza.sleep(intervaloPausaMinutos * 60 * 1000);

                } catch (Exception ex) {
                    trayIcon.setImage(imagePausa);
                    System.exit(0);
                }
            }
        }
    }
}
