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

public class Model 
{
    public void print(ArrayList tabelas, String nomeProjeto)
    {
        String diretorioMenu = nomeProjeto + "\\application\\models\\DbTable\\";
        
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
            for(int i = 0; i < tabelas.size(); i++)
            {
                String listaDeModels = "";
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
                
                PrintWriter writer = new PrintWriter(diretorioMenu + novaString + ".php", "UTF-8");
                writer.println("<?php");
                writer.println("");
                writer.println("class Application_Model_DbTable_" + novaString + " extends Zend_Db_Table_Abstract");
                writer.println("{");
                writer.println("    protected $_name = 'tbl_" + tabelas.get(i) + "';");
                writer.println("}");

                writer.close();
                System.out.println("Model criada com sucesso!");
            }
        }
        catch(FileNotFoundException| UnsupportedEncodingException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
