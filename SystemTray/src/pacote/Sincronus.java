package pacote;

/**
 * <p>
 * Title: Infocus ERP - Projeto Acadêmico</p>
 *
 * @version	versão
 * @param	parâmetro do método
 * @return	retorno do método
 * @exception	exceção
 * @see	veja também
 *
 * <p>
 *
 * <p>
 * The MIT License</p>
 * <p>
 * Copyright: Copyright (C) 2011 Raul Lopes</p>
 *
 * @author
 * <p>
 * Raul Lopes The author may be contacted at: raul.lagoa@gmail.com</p>
 *
 */
import javax.swing.JFrame;

/**
 *
 * @author PC
 */
public class Sincronus extends JFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Sincronus();
    }

    Sincronus() {
        
        try {

            versaoAutomatica();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //executa em modo stand a lone
    private static void versaoAutomatica() {
        new ExecucaoAutomatica();
    }

}
