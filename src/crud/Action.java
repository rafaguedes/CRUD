package crud;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Action 
{
    public void print(ArrayList tabelas, String nomeProjeto, Connection connection)
    {
        String diretorioMenu = nomeProjeto + "\\application\\controllers\\";
        
        File file = new File(diretorioMenu);
        if (!file.exists()) 
        {
            if (file.mkdirs()) 
            {
                System.out.println("Directory is created!");
            } 
            else 
            {
                System.out.println("Failed to create directory!");
            }
	}
        
        try
        {
            PrintWriter writer = new PrintWriter(diretorioMenu + "IndexController.php", "UTF-8");
            writer.println("<?php");
            writer.println("");
            writer.println("class IndexController extends Zend_Controller_Action");
            writer.println("{");
            writer.println("");
            writer.println("    public function init()");
            writer.println("    {");
            
            String listaDeModels = "";
            for(int i = 0; i < tabelas.size(); i++)
            {
                String sString = tabelas.get(i).toString().toLowerCase();
                sString = Character.toString(sString.charAt(0)).toUpperCase()+sString.substring(1);
                
                String[] partes = sString.split("_");
                
                String novaString = "";
                for(int j = 0; j < partes.length; j++)
                {
                    String temp = partes[j].toLowerCase();
                    temp = Character.toString(temp.charAt(0)).toUpperCase()+temp.substring(1);
                    novaString += temp;
                }
                
                listaDeModels += "$model" + novaString + " = new Application_Model_DbTable_" + novaString + ";\n";
            }
            
            String noLayouts = "";
            for(int i = 0; i < tabelas.size(); i++)
            {
                noLayouts += "'c-" + tabelas.get(i) + "',";
                noLayouts += "'r-" + tabelas.get(i) + "',";
                noLayouts += "'u-" + tabelas.get(i) + "',";
                noLayouts += "'d-" + tabelas.get(i) + "',";
            }
            
            writer.println("        $noLayouts = array('verifica-login', 'sair', " + noLayouts + ");");
            writer.println("");
            writer.println("        if (in_array($this->_getParam('action'), $noLayouts))");
            writer.println("        {");
            writer.println("            $this->_helper->viewRenderer->setNoRender(true);");
            writer.println("            $this->_helper->layout()->disableLayout();");
            writer.println("        }");
            writer.println("");
            writer.println("        $this->view->page = $this->_getParam('action');");
            writer.println("        define('CONTROLLER','index');");
            writer.println("        date_default_timezone_set('America/Sao_Paulo');");
            writer.println("    }");
            writer.println("");
            writer.println("    public function indexAction()");
            writer.println("    {");
            writer.println("        if(!Zend_Auth::getInstance()->hasIdentity())");
            writer.println("        {");
            writer.println("            $this->_redirect(BASE_URL . 'index/login');");
            writer.println("        }");
            writer.println("");
            writer.println("        $modelUsuarios = new Application_Model_DbTable_Usuarios();");
            writer.println("        " + listaDeModels);
            writer.println("        $login = Zend_Auth::getInstance()->getIdentity()->login;");
            writer.println("        $usuario = $modelUsuarios->fetchRow(\"login = '\" . $login . \"'\");");
            writer.println("        $this->view->usuario = $usuario;");
            writer.println("    }");
            writer.println("");
            writer.println("    public function loginAction()");
            writer.println("    {");
            writer.println("        if(Zend_Auth::getInstance()->hasIdentity())");
            writer.println("        {");
            writer.println("            $this->_redirect(BASE_URL);");
            writer.println("        }");
            writer.println("    }");
            writer.println("");
            writer.println("    public function verificaLoginAction()");
            writer.println("    {");
            writer.println("        $retorno = array();");
            writer.println("");
            writer.println("        $retorno['status'] = false;");
            writer.println("");
            writer.println("        $login = $_POST['login'];");
            writer.println("        $senha = $_POST['senha'];");
            writer.println("");
            writer.println("        if ($login == \"\") {");
            writer.println("            $retorno['message'] = \"O campo LOGIN é obrigatório!\";");
            writer.println("            echo json_encode($retorno);");
            writer.println("            exit;");
            writer.println("        }");
            writer.println("");
            writer.println("        if ($senha == \"\") {");
            writer.println("            $retorno['message'] = \"O campo SENHA é obrigatório!\";");
            writer.println("            echo json_encode($retorno);");
            writer.println("            exit;");
            writer.println("        }");
            writer.println("");
            writer.println("        $objAuth = Zend_Auth::getInstance();");
            writer.println("");
            writer.println("        $authAdapter = new Zend_Auth_Adapter_DbTable(");
            writer.println("            Zend_Db_Table::getDefaultAdapter(),");
            writer.println("            'tbl_usuarios',");
            writer.println("            'login',");
            writer.println("            'senha',");
            writer.println("            ''");
            writer.println("        );");
            writer.println("");
            writer.println("        $retorno['message'] = \"Login inválido\";");
            writer.println("");
            writer.println("        $authAdapter->setIdentity($login)->setCredential($senha);");
            writer.println("        $result = $objAuth->authenticate($authAdapter);");
            writer.println("");
            writer.println("        $retorno['status'] = false;");
            writer.println("        if($result->isValid())");
            writer.println("        {");
            writer.println("            $authData = $authAdapter->getResultRowObject();");
            writer.println("            $objAuth->getStorage()->write($authData);");
            writer.println("            $retorno['status'] = true;");
            writer.println("        }");
            writer.println("");
            writer.println("        echo json_encode($retorno);");
            writer.println("");
            writer.println("    }");
            writer.println("");
            writer.println("    public function sairAction()");
            writer.println("    {");
            writer.println("        $session = new Zend_Session_Namespace();");
            writer.println("        Zend_Auth::getInstance()->clearIdentity();");
            writer.println("");
            writer.println("        $session->unsetAll();");
            writer.println("");
            writer.println("        $this->_redirect(BASE_URL . CONTROLLER . '/index');");
            writer.println("");
            writer.println("    }");
            writer.println("");
                    
            for(int i = 0; i < tabelas.size(); i++)
            {
                String sString = tabelas.get(i).toString().toLowerCase();
                
                String[] partes = sString.split("_");
                
                String novaString = "";
                for(int j = 0; j < partes.length; j++)
                {
                    String temp = partes[j].toLowerCase();
                    temp = Character.toString(temp.charAt(0)).toUpperCase()+temp.substring(1);
                    novaString += temp;
                }
                
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM tbl_" + tabelas.get(i));              
                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount(); //number of column
                
                String columnName[] = new String[count];
                String columnComment[] = new String[count];
                
                String titulo = Character.toString(novaString.charAt(0)).toLowerCase() + novaString.substring(1);
                String nomeClasse = Character.toString(novaString.charAt(0)).toUpperCase() + novaString.substring(1);
                
                writer.println("    public function " + titulo + "Action()");
                writer.println("    {");
                writer.println("");
                writer.println("        $retorno = array();");
                writer.println("        $retorno['status'] = false;");
                writer.println("");
                writer.println("        if(!Zend_Auth::getInstance()->hasIdentity())");
                writer.println("        {");
                writer.println("            $this->_redirect(BASE_URL . 'index/login');");
                writer.println("        }");
                writer.println("");
                writer.println("        $modelUsuarios = new Application_Model_DbTable_Usuarios();");
                writer.println("        " + listaDeModels);
                writer.println("        $login = Zend_Auth::getInstance()->getIdentity()->login;");
                writer.println("        $usuario = $modelUsuarios->fetchRow(\"login = '\" . $login . \"'\");");
                writer.println("        $this->view->usuario = $usuario;");
                writer.println("");
                writer.println("        $" + titulo + " = $model" + nomeClasse + "->fetchAll('excluido = 0');");
                writer.println("        $this->view->" + titulo + " = $" + titulo + ";");
                writer.println("    }");
                writer.println("");
     
                writer.println("    public function c" + novaString + "Action()");
                writer.println("    {");
                writer.println("");
                writer.println("        $retorno = array();");
                writer.println("        $retorno['status'] = false;");
                writer.println("");
                writer.println("        if(!Zend_Auth::getInstance()->hasIdentity())");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"O LOGIN é obrigatório!\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("");
                writer.println("        $modelUsuario = new Application_Model_DbTable_Usuarios();");
                writer.println("        " + listaDeModels);
                writer.println("        $login = Zend_Auth::getInstance()->getIdentity()->login;");
                writer.println("        ");
                writer.println("        $usuario = $modelUsuario->fetchRow(\"login = '\" . $login . \"' AND admin = 1\");");
                writer.println("        ");
                writer.println("        if(!$usuario)");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"O USUÁRIO não foi encontrado\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("        ");
                
                writer.println("        $dados = array();");
                for (int k = 1; k <= count; k++)
                {
                    columnName[k-1] = metaData.getColumnLabel(k);
                    
                    if(!columnName[k-1].toLowerCase().equals("id"))
                    {
                        writer.println("        if(!isset($_POST['" + columnName[k-1].toLowerCase() + "']))");
                        writer.println("        {");
                        writer.println("            $retorno['message'] = \"" + columnName[k-1].toLowerCase() + " não foi enviado!\";");
                        writer.println("            echo json_encode($retorno);");
                        writer.println("            exit;");
                        writer.println("        }");
                        writer.println("        else");
                        writer.println("        {");
                        writer.println("            if($_POST['" + columnName[k-1].toLowerCase() + "'] == '')");
                        writer.println("            {");
                        writer.println("                $retorno['message'] = \"" + columnName[k-1].toLowerCase() + " não foi enviado!\";");
                        writer.println("                echo json_encode($retorno);");
                        writer.println("                exit;");
                        writer.println("            }");
                        writer.println("        }");
                        writer.println("        $dados['" + columnName[k-1].toLowerCase() + "'] = $_POST['" + columnName[k-1].toLowerCase() + "'];");
                        writer.println("");
                    }
                }
                
                writer.println("");
                writer.println("        $model" + nomeClasse + "->insert($dados);");
                writer.println("");
                writer.println("        $retorno['status'] = true;");
                writer.println("        echo json_encode($retorno);");
                
                writer.println("        ");
                writer.println("    }");
                writer.println("");
                
                
                writer.println("    public function r" + novaString + "Action()");
                writer.println("    {");
                writer.println("");
                writer.println("        $retorno = array();");
                writer.println("        $retorno['status'] = false;");
                writer.println("");
                writer.println("        if(!Zend_Auth::getInstance()->hasIdentity())");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"O LOGIN é obrigatório!\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("        $modelUsuario = new Application_Model_DbTable_Usuarios();");
                writer.println("");
                writer.println("        $login = Zend_Auth::getInstance()->getIdentity()->login;");
                writer.println("        ");
                writer.println("        $usuario = $modelUsuario->fetchRow(\"login = '\" . $login . \"' AND admin = 1\");");
                writer.println("        ");
                writer.println("        if(!$usuario)");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"O USUÁRIO não foi encontrado\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("");
                writer.println("        if(!isset($_POST['id']))");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"O ID não foi enviado!\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("        else");
                writer.println("        {");
                writer.println("            if($_POST['id'] == '')");
                writer.println("            {");
                writer.println("                $retorno['message'] = \"O ID não foi enviado!\";");
                writer.println("                echo json_encode($retorno);");
                writer.println("                exit;");
                writer.println("            }");
                writer.println("        }");
                writer.println("        $id = $_POST['id'];");
                writer.println("");
                writer.println("        " + listaDeModels);
                writer.println("        $" + titulo + " = $model" + nomeClasse + "->fetchRow(\"id = \" . $id);");
                writer.println("");
                writer.println("        if(!$" + titulo + ")");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"O ID não foi enviado!\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("        $temp = $" + titulo + ";");
                writer.println("");
                
                for (int k = 1; k <= count; k++)
                {
                    columnName[k-1] = metaData.getColumnLabel(k);
                    writer.println("        $retorno['" + metaData.getColumnLabel(k) + "'] = $temp->" + metaData.getColumnLabel(k) + ";");
                }
                
                writer.println("");
                writer.println("        $retorno['status'] = true;");
                writer.println("        echo json_encode($retorno);");
                writer.println("");
                writer.println("    }");
                writer.println("");
                
                writer.println("    public function u" + novaString + "Action()");
                writer.println("    {");
                writer.println("        if(!Zend_Auth::getInstance()->hasIdentity())");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"O LOGIN é obrigatório!\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("");
                writer.println("        $modelUsuario = new Application_Model_DbTable_Usuarios();");
                writer.println("");
                writer.println("        $login = Zend_Auth::getInstance()->getIdentity()->login;");
                writer.println("        ");
                writer.println("        $usuario = $modelUsuario->fetchRow(\"login = '\" . $login . \"' AND admin = 1\");");
                writer.println("        " + listaDeModels);
                writer.println("        if(!$usuario)");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"O USUÁRIO não foi encontrado\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("        ");
                writer.println("        if(!isset($_POST['id']))");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"O ID não foi enviado!\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("        else");
                writer.println("        {");
                writer.println("            if($_POST['id'] == '')");
                writer.println("            {");
                writer.println("                $retorno['message'] = \"O ID não foi enviado!\";");
                writer.println("                echo json_encode($retorno);");
                writer.println("                exit;");
                writer.println("            }");
                writer.println("        }");
                writer.println("        $id = $_POST['id'];");
                writer.println("");
                writer.println("        $" + titulo + " = $model" + nomeClasse + "->fetchRow(\"id = \" . $id);");
                writer.println("");
                writer.println("        if(!$" + titulo + ")");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"O ID não foi enviado!\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("        $temp = $" + titulo + ";");
                writer.println("");
                for (int k = 1; k <= count; k++)
                {
                    columnName[k-1] = metaData.getColumnLabel(k);
                    
                    if(!columnName[k-1].toLowerCase().equals("id"))
                    {
                        writer.println("        if(!isset($_POST['" + columnName[k-1] + "']))");
                        writer.println("        {");
                        writer.println("            $retorno['message'] = \"" + columnName[k-1] + " não foi enviado!\";");
                        writer.println("            echo json_encode($retorno);");
                        writer.println("            exit;");
                        writer.println("        }");
                        writer.println("        else");
                        writer.println("        {");
                        writer.println("            if($_POST['" + columnName[k-1] + "'] == '')");
                        writer.println("            {");
                        writer.println("                $retorno['message'] = \"" + columnName[k-1] + " não foi enviado!\";");
                        writer.println("                echo json_encode($retorno);");
                        writer.println("                exit;");
                        writer.println("            }");
                        writer.println("        }");
                        writer.println("        $temp->" + columnName[k-1] + " = $_POST['" + columnName[k-1] + "'];");
                        writer.println("");
                    }
                }
                writer.println("        $temp->save();");
                writer.println("");
                writer.println("        $retorno['status'] = true;");
                writer.println("        echo json_encode($retorno);");
                writer.println("");
                
                writer.println("    }");
                writer.println("");
                writer.println("    public function d" + novaString + "Action()");
                writer.println("    {");
                writer.println("");
                writer.println("        $modelUsuario = new Application_Model_DbTable_Usuarios();");
                writer.println("");
                writer.println("        $retorno = array();");
                writer.println("        $retorno['status'] = false;");
                writer.println("");
                writer.println("        if(!Zend_Auth::getInstance()->hasIdentity())");
                writer.println("        {");
                writer.println("");
                writer.println("            $retorno['message'] = \"Erro de login!\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("");
                writer.println("        $login = Zend_Auth::getInstance()->getIdentity()->login;");
                writer.println("        $usuario = $modelUsuario->fetchRow(\"login = '\" . $login . \"' AND admin = 1\");");
                writer.println("");
                writer.println("        if(!$usuario)");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"Erro de login\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("");
                writer.println("        if(!isset($_POST['id']))");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"O ID não foi enviado!\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("        $id = $_POST['id'];");
                writer.println("");
                writer.println("        $model" + novaString + " = new Application_Model_DbTable_" + novaString + "();");
                writer.println("");
                writer.println("        $temp = $model" + novaString + "->fetchRow(\"id = \" . $id);");
                writer.println("");
                writer.println("        if(!$temp)");
                writer.println("        {");
                writer.println("            $retorno['message'] = \"Item não foi encontrado!\";");
                writer.println("            echo json_encode($retorno);");
                writer.println("            exit;");
                writer.println("        }");
                writer.println("");
                writer.println("        $temp->excluido = 1;");
                writer.println("        $temp->save();");
                writer.println("");
                writer.println("        $retorno['status'] = true;");
                writer.println("        echo json_encode($retorno);");
                writer.println("    }");
                
                writer.println("    public function cadastrar" + novaString + "Action()");
                writer.println("    {");
                writer.println("        if(!Zend_Auth::getInstance()->hasIdentity())");
                writer.println("        {");
                writer.println("            $this->_redirect(BASE_URL . 'index/login');");
                writer.println("        }");
                writer.println("");
                writer.println("        $modelUsuarios = new Application_Model_DbTable_Usuarios();");
                writer.println("        " + listaDeModels);
                writer.println("        $login = Zend_Auth::getInstance()->getIdentity()->login;");
                writer.println("        $usuario = $modelUsuarios->fetchRow(\"login = '\" . $login . \"'\");");
                writer.println("        $this->view->usuario = $usuario;");
                writer.println("    }");
            }
            
            writer.println("}");
            
            writer.close();
            System.out.println("Action criado com sucesso!");
            
        }
        catch(FileNotFoundException | UnsupportedEncodingException | SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
