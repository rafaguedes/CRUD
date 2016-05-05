package crud;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Menu 
{
    public void print(ArrayList tabelas, String nomeProjeto)
    {
        String diretorioMenu = nomeProjeto + "\\application\\views\\scripts\\index\\includes\\";
        
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
            PrintWriter writer = new PrintWriter(diretorioMenu + "menu.phtml", "UTF-8");
            writer.println("<?php");
            writer.println("    $page = $this->page;");
            writer.println("?>");
            writer.println("");
            
            writer.println("<div class=\"navbar-brand\">");
            writer.println("    <a href=\"<?=BASE_URL?>\">");
            writer.println("        <img src=\"<?=BASE_URL?>public/assets/images/logo@2x.png\" width=\"88\" alt=\"\" />");
            writer.println("    </a>");
            writer.println("</div>");
            
            for(int i = 0; i < tabelas.size(); i++)
            {
                String tabelaMomento = tabelas.get(i).toString();

                int counter = 0;
                for(int j = 0; j < tabelaMomento.length(); j++) 
                {
                    if(tabelaMomento.charAt(j) == '_') 
                    {
                        counter++;
                    } 
                }
                
                System.out.println(counter);
                if(counter == 0)
                {
                    String temp = tabelas.get(i).toString();

                    String endereco = tabelas.get(i).toString().toLowerCase().replace("_", "-");
                    String[] partes = tabelas.get(i).toString().split("_");

                    String novaString = "";
                    for(int j = 0; j < partes.length; j++)
                    {
                        temp = partes[j].toLowerCase();
                        temp = Character.toString(temp.charAt(0)).toUpperCase()+temp.substring(1);

                        if((j + 1) < partes.length)
                        {
                            novaString += temp + " ";
                        }
                        else
                        {
                            novaString += temp;
                        }
                    }
                    String nome = novaString;

                    writer.println("<li <?php if($page == '" + endereco + "') { echo 'class=\"active\"'; } ?>>");
                    writer.println("    <a href=\"#\">");
                    writer.println("    <i class=\"glyphicon glyphicon-home\"></i>");
                    writer.println("    <span class=\"title\">" + nome + "</span>");
                    writer.println("    </a>");
                    writer.println("    <ul <?php if($page == '" + endereco + "') { echo 'class=\"visible\"'; } ?>>");
                    writer.println("        <li <?php if($page == '" + endereco + "') { echo 'class=\"active\"'; } ?>>");
                    writer.println("            <a href=\"<?= BASE_URL ?>index/" + endereco + "\">");
                    writer.println("                <span class=\"title\">Listar " + nome + "</span>");
                    writer.println("            </a>");
                    writer.println("        </li>");
                    writer.println("        <li <?php if($page == '" + endereco + "') { echo 'class=\"active\"'; } ?>>");
                    writer.println("            <a href=\"<?= BASE_URL ?>index/cadastrar-" + endereco + "\">");
                    writer.println("                <span class=\"title\">Cadastrar " + nome + "</span>");
                    writer.println("            </a>");
                    writer.println("        </li>");
                    writer.println("    </ul>");
                    writer.println("</li>");
                }
                System.out.println("Menu criado com sucesso!");
            }   
            
            writer.close();
            
        }
        catch(FileNotFoundException | UnsupportedEncodingException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
