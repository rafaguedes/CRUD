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

public class View 
{
    public void print(ArrayList tabelas, String nomeProjeto, Connection connection)
    {
        String diretorioMenu = nomeProjeto + "\\application\\views\\scripts\\index\\";
        
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
                String temp = "_";
                if(tabelas.get(i).toString().indexOf("_") == -1)
                {
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery("SELECT * FROM tbl_" + tabelas.get(i));              
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int count = metaData.getColumnCount(); //number of column
                
                    String columnName[] = new String[count];
                
                    PrintWriter writer = new PrintWriter(diretorioMenu + tabelas.get(i) + ".phtml", "UTF-8");
                    writer.println("<?php");
                    writer.println("");
                    writer.println("    $page = $this->page;");
                    writer.println("    $" + tabelas.get(i) + " = $this->" + tabelas.get(i) + ";");
                    writer.println("    $usuario = $this->usuario;");
                    
                    String listaDeModels = "";
                    for(int n = 0; n < tabelas.size(); n++)
                    {
                        String sString = tabelas.get(n).toString().toLowerCase();
                        sString = Character.toString(sString.charAt(0)).toUpperCase() + sString.substring(1);

                        String[] partes = sString.split("_");

                        String novaString = "";
                        for(int j = 0; j < partes.length; j++)
                        {
                            temp = partes[j].toLowerCase();
                            temp = Character.toString(temp.charAt(0)).toUpperCase() + temp.substring(1);
                            novaString += temp;
                        }

                        listaDeModels += "$model" + novaString + " = new Application_Model_DbTable_" + novaString + ";\n";
                    }
                    
                    writer.println("    " + listaDeModels);
                    
                    String sString = tabelas.get(i).toString().toLowerCase();
                    String[] partes = sString.split("_");
                    String novaString = "";
                    for(int j = 0; j < partes.length; j++)
                    {
                        temp = partes[j].toLowerCase();
                        temp = Character.toString(temp.charAt(0)).toUpperCase()+temp.substring(1);
                        novaString += temp;
                    }

                    writer.println("?>");
                    writer.println("");
                    writer.println("<style>");
                    writer.println(".page-container.horizontal-menu header.navbar.navbar-fixed-top { z-index: 100; }");
                    writer.println("</style>");
                    writer.println("");
                    writer.println("<body class=\"page-body page-fade\" data-url=\"http://www.yourapp.com.br\">");
                    writer.println("    <div class=\"page-container horizontal-menu\">");
                    writer.println("        <header class=\"navbar navbar-fixed-top\">");
                    writer.println("            <div class=\"navbar-inner\">");
                    writer.println("                <ul class=\"navbar-nav\">");
                    writer.println("                    <?php echo $this->render(CONTROLLER . '/includes/menu.phtml'); ?>");
                    writer.println("                </ul>");
                    writer.println("            </div>");
                    writer.println("        </header>");
                    writer.println("        <div class=\"main-content\">");
                    writer.println("            <div class=\"container\">");
                    writer.println("                <div class=\"row\">");
                    writer.println("                    <div class=\"col-md-6 col-sm-8 clearfix\">");
                    writer.println("                        <ul class=\"user-info pull-left pull-none-xsm\">");
                    writer.println("                            <li class=\"profile-info dropdown\">");
                    writer.println("                                <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">");
                    writer.println("                                    <img src=\"http://app.hireme.com.br/uploads/nophoto.jpg\" alt=\"\" class=\"img-circle\" width=\"44\" /><?= $usuario->nome ?>");
                    writer.println("                                </a>");
                    writer.println("                                <ul class=\"dropdown-menu\">");
                    writer.println("                                    <li class=\"caret\"></li>");
                    writer.println("                                    <li>");
                    writer.println("                                        <a href=\"#\">");
                    writer.println("                                            <i class=\"entypo-user\"></i>");
                    writer.println("                                            Editar sua conta");
                    writer.println("                                        </a>");
                    writer.println("                                    </li>");
                    writer.println("                                </ul>");
                    writer.println("                            </li>");
                    writer.println("                        </ul>");
                    writer.println("                        <ul class=\"user-info pull-left pull-right-xs pull-none-xsm\">");
                    writer.println("                        </ul>");
                    writer.println("                    </div>");
                    writer.println("                    <div class=\"col-md-6 col-sm-4 clearfix hidden-xs\">");
                    writer.println("                        <ul class=\"list-inline links-list pull-right\">");
                    writer.println("                            <li class=\"sep\"></li>");
                    writer.println("                            <li class=\"sep\"></li>");
                    writer.println("                            <li>");
                    writer.println("                                <a href=\"<?= BASE_URL . CONTROLLER ?>/sair\">");
                    writer.println("                                    Sair do Sistema <i class=\"entypo-logout right\"></i>");
                    writer.println("                                </a>");
                    writer.println("                            </li>");
                    writer.println("                        </ul>");
                    writer.println("                    </div>");
                    writer.println("                </div>");
                    writer.println("                <h3 style=\"font-family: 'Open Sans Condensed', sans-serif; font-size: 32px;\">Lista de todos " + novaString + "</h3>");
                    writer.println("                <br />");
                    writer.println("                <table class=\"table table-bordered datatable\" id=\"table-3\">");
                    writer.println("                    <thead>");
                    writer.println("                        <tr class=\"replace-inputs\">");
                    for (int k = 1; k <= count; k++)
                    {
                        writer.println("                            <th>" + metaData.getColumnLabel(k).replace("id_", "") + "</th>");
                    }
                    writer.println("                        </tr>");
                    writer.println("                        <tr>");
                    for (int k = 1; k <= count; k++)
                    {
                        writer.println("                            <th>" + metaData.getColumnLabel(k).replace("id_", "") + "</th>");
                    }
                    writer.println("                        <td></td>");
                    writer.println("                        </tr>");
                    writer.println("                    </thead>");
                    writer.println("                <tbody>");
                    writer.println("                <?php");
                    writer.println("                if(sizeof($" + tabelas.get(i)  + ") > 0)");
                    writer.println("                {");
                    writer.println("");
                    writer.println("                    foreach($" + tabelas.get(i) + " as $temp)");
                    writer.println("                    {");
                    writer.println("                    ?>");
                    writer.println("                    <tr class=\"odd gradeX\">");
                    for (int k = 1; k <= count; k++)
                    {
                        writer.println("                    <td><?=$temp->" + metaData.getColumnLabel(k) + "?></td>");
                    }
                    writer.println("                        <td>");
                    writer.println("                            <a class=\"botaoEditar btn btn-default btn-sm btn-icon icon-left\" id=\"<?=$temp->id?>\">");
                    writer.println("                                <i style=\"padding-top:8px;\" class=\"entypo-pencil\"></i>");
                    writer.println("                                Editar");
                    writer.println("                            </a>");
                    writer.println("                            <a class=\"botaoExcluir btn btn-danger btn-sm btn-icon icon-left\" id=\"<?=$temp->id?>\">");
                    writer.println("                                <i style=\"padding-top:8px;\" class=\"entypo-cancel\"></i>");
                    writer.println("                                Excluir");
                    writer.println("                            </a>");
                    writer.println("                        </td>");
                    writer.println("                    </tr>");
                    writer.println("                    <?php");
                    writer.println("                    }");
                    writer.println("                }");
                    writer.println("                ?>");
                    writer.println("                </tbody>");
                    writer.println("                <tfoot>");
                    writer.println("                    <tr>");
                    for (int k = 1; k <= count; k++)
                    {
                        writer.println("                    <th>" + metaData.getColumnLabel(k).replace("id_", "") + "</th>");
                    }
                    writer.println("                        <td></td>");
                    writer.println("                    </tr>");
                    writer.println("                </tfoot>");
                    writer.println("            </table>");
                    writer.println("");
                    writer.println("            <script type=\"text/javascript\">");
                    writer.println("            $(function()");
                    writer.println("            {");
                    writer.println("                var table = $(\"#table-3\").dataTable({");
                    writer.println("                    \"sPaginationType\": \"bootstrap\",");
                    writer.println("                    \"aLengthMenu\": [[10, 25, 50, -1], [10, 25, 50, \"All\"]],");
                    writer.println("                    \"bStateSave\": true");
                    writer.println("                });");
                    writer.println("");
                    writer.println("                table.columnFilter({");
                    writer.println("                    \"sPlaceHolder\" : \"head:after\"");
                    writer.println("                });");
                    writer.println("            });");
                    writer.println("            </script>");
                    writer.println("");
                    writer.println("            <footer class=\"main\">");
                    writer.println("                &copy; 2015 YourApp Inc. by <a href=\"http://www.yourapp.com.br\" target=\"_blank\">YourApp</a>");
                    writer.println("            </footer>");
                    writer.println("        </div>");
                    writer.println("    </div>");
                    writer.println("</div>");
                    
                    writer.println("<div class=\"modal fade\" id=\"edit\">");
                    writer.println("    <div class=\"modal-dialog\">");
                    writer.println("        <div class=\"modal-content\">");
                    writer.println("            <div class=\"modal-header\">");
                    writer.println("                <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>");
                    writer.println("                <h4 class=\"modal-title\">Editar " + novaString + "</h4>");
                    writer.println("            </div>");
                    writer.println("            <div class=\"modal-body\">");
                    writer.println("                <div class=\"row\">");
                    writer.println("                    <div class=\"col-md-12 center-block\" style=\"text-align: center;\" id=\"loading\">");
                    writer.println("                        <h1 style=\"font-family: 'Open Sans Condensed', sans-serif;\">Carregando...</h1>");
                    writer.println("                        <img src=\"<?=BASE_URL?>public/images/loading.gif\">");
                    writer.println("                     </div>");
                    writer.println("                     <div id=\"dados\" style=\"display: none;\">");
                    writer.println("                        <form id=\"form\" class=\"form-horizontal form-groups-bordered\" role=\"form\" style=\"overflow:hidden; padding-left: 30px; padding-right: 30px;\">");
                    
                    for (int k = 1; k <= count; k++)
                    {
                        if(metaData.getColumnLabel(k).contains("id_"))
                        {
                            String nomeTabela = metaData.getColumnLabel(k).replace("id_", "");
                            String titulo = Character.toString(nomeTabela.charAt(0)).toUpperCase() + nomeTabela.substring(1);
                            
                            String sStringTemp = nomeTabela.toLowerCase();
                            sStringTemp = Character.toString(sStringTemp.charAt(0)).toUpperCase() + sStringTemp.substring(1);

                            String[] partesTemp = sStringTemp.split("_");

                            String novaStringTemp = "";
                            for(int j = 0; j < partesTemp.length; j++)
                            {
                                String tempMomento = partesTemp[j].toLowerCase();
                                tempMomento = Character.toString(tempMomento.charAt(0)).toUpperCase() + tempMomento.substring(1);
                                novaStringTemp += tempMomento;
                            }
                                    
                            writer.println("                        <div class=\"form-group\">");
                            writer.println("                            <label class=\"col-sm-3 control-label\" for=\"field-1\">" + titulo + "</label>");
                            writer.println("                            <div class=\"col-sm-12\">");
                            writer.println("                                <select id=\"" + metaData.getColumnLabel(k) + "\" class=\"select2\" data-allow-clear=\"true\" data-placeholder=\"Selecione...\">");
                            writer.println("                                    <?php");
                            writer.println("");                                 
                            
                            if(novaStringTemp.charAt(novaStringTemp.length() - 1) != 's')
                            {
                                writer.println("                                    $" + metaData.getColumnLabel(k).replace("id_", "") + "s = $model" + novaStringTemp + "s->fetchAll();");
                            }
                            else
                            {
                                writer.println("                                    $" + metaData.getColumnLabel(k).replace("id_", "") + "s = $model" + novaStringTemp + "->fetchAll();");
                            }
                            
                            writer.println("");
                            writer.println("                                    if(sizeof($" + metaData.getColumnLabel(k).replace("id_", "") + "s) > 0)");
                            writer.println("                                    {");
                            writer.println("                                        foreach($" + metaData.getColumnLabel(k).replace("id_", "") + "s as $temp)");
                            writer.println("                                        {");
                            writer.println("                                        ?>");
                            writer.println("                                            <option value=\"<?=$temp->id?>\"><?=$temp->nome?></option>");
                            writer.println("                                        <?php");
                            writer.println("                                        }");
                            writer.println("                                    }");
                            writer.println("                                    ?>");
                            writer.println("                                </select>");
                            writer.println("                            </div>");
                            writer.println("                        </div>");
                        }
                        else
                        {
                            if(!metaData.getColumnLabel(k).equals("id"))
                            {
                                if(!metaData.getColumnLabel(k).equals("data"))
                                {
                                    writer.println("                        <div class=\"form-group\">");
                                    writer.println("                            <label class=\"col-sm-3 control-label\" for=\"field-1\">" + metaData.getColumnLabel(k).replace("_", " ") + "</label>");
                                    writer.println("                            <div class=\"col-sm-9\">");
                                    writer.println("                                <input type=\"text\" placeholder=\"Digitar o " + metaData.getColumnLabel(k) + "\" id=\"" + metaData.getColumnLabel(k) + "\" class=\"form-control\">");
                                    writer.println("                            </div>");
                                    writer.println("                        </div>");
                                }
                                else
                                {
                                    writer.println("                        <div class=\"form-group\">");
                                    writer.println("                            <label class=\"col-sm-3 control-label\" for=\"field-1\">" + metaData.getColumnLabel(k).replace("_", " ") + "</label>");
                                    writer.println("                            <div style=\"padding:0; margin:0;\"class=\"input-group col-sm-3\">");
                                    writer.println("                                <input id=\"data\" type=\"text\" class=\"form-control datepicker\" data-format=\"D, yyyy MM dd\">");
                                    writer.println("                                <div class=\"input-group-addon\">");
                                    writer.println("                                    <a href=\"#\"><i class=\"entypo-calendar\"></i></a>");
                                    writer.println("                                </div>");
                                    writer.println("                            </div>");
                                    writer.println("                        </div>");
                                }
                            }
                            else
                            {
                                writer.println("                        <input type=\"hidden\" id=\"" + metaData.getColumnLabel(k) + "\" class=\"form-control\">");
                            }
                        }
                    }

                    writer.println("                        </form>");
                    writer.println("                    </div>");
                    writer.println("                </div>");
                    writer.println("            </div>");
                    writer.println("            <div class=\"modal-footer\">");
                    writer.println("                <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Fechar</button>");
                    writer.println("                <button type=\"button\" id=\"botaoSalvar\" class=\"btn btn-info\">Salvar alterações</button>");
                    writer.println("            </div>");
                    writer.println("        </div>");
                    writer.println("    </div>");
                    writer.println("</div>");
                    
                    writer.println("");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/jcrop/jquery.Jcrop.min.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/datatables/responsive/css/datatables.responsive.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/select2/select2-bootstrap.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/select2/select2.css\">");
                    writer.println("");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/bootstrap-datepicker.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/bootstrap-timepicker.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/bootstrap-colorpicker.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/gsap/main-gsap.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/jquery-ui/js/jquery-ui-1.10.3.minimal.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/bootstrap.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/joinable.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/resizeable.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/neon-api.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/jquery.dataTables.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/datatables/TableTools.min.js\"></script>");
                    writer.println("");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/dataTables.bootstrap.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/datatables/jquery.dataTables.columnFilter.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/datatables/lodash.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/datatables/responsive/js/datatables.responsive.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/select2/select2.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/neon-chat.js\"></script>");
                    writer.println("");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/jcrop/jquery.Jcrop.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/neon-custom.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/neon-demo.js\"></script>");
                    writer.println("");
                    writer.println("    <script type=\"text/javascript\">");
                    writer.println("    $(function()");
                    writer.println("    {");
                    writer.println("");
                    writer.println("        var opts = { \"closeButton\": true, \"debug\": false, \"positionClass\": \"toast-top-right\", \"onclick\": null, \"showDuration\": \"300\", \"hideDuration\": \"1000\", \"timeOut\": \"5000\", \"extendedTimeOut\": \"1000\", \"showEasing\": \"swing\", \"hideEasing\": \"linear\", \"showMethod\": \"fadeIn\", \"hideMethod\": \"fadeOut\"};");
                    writer.println("");
                    writer.println("        $(\".botaoExcluir\").click(function()");
                    writer.println("        {");
                    writer.println("");
                    writer.println("            var id = $(this).attr(\"id\");");
                    writer.println("            var tempthis = $(this);");
                    writer.println("");
                    writer.println("            var retorno = confirm(\"Você tem certeza que deseja excluir?\");");
                    writer.println("            if(retorno)");
                    writer.println("            {");
                    writer.println("");
                    writer.println("                show_loading_bar(65);");
                    writer.println("");
                    writer.println("                $.ajax({");
                    writer.println("                    url: '<?=BASE_URL.CONTROLLER?>/d-" + tabelas.get(i) + "',");
                    writer.println("                    type: 'POST',");
                    writer.println("                    data: ");
                    writer.println("                    {");
                    writer.println("                        id: id");
                    writer.println("                    },");
                    writer.println("                    dataType: \"json\",");
                    writer.println("                    beforeSend: function() {},");
                    writer.println("                    complete: function() {},");
                    writer.println("                    success: function(retorno)");
                    writer.println("                    {");
                    writer.println("                        show_loading_bar({");
                    writer.println("                            pct: 100,");
                    writer.println("                            finish: function(pct)");
                    writer.println("                            {");
                    writer.println("                                if(retorno.status)");
                    writer.println("                                {");
                    writer.println("                                    toastr.success(\"Exclusão efetuada com sucesso!\", \"Feito!\", opts);");
                    writer.println("                                    tempthis.parent().parent().fadeOut(\"slow\");");
                    writer.println("                                }");
                    writer.println("                                else");
                    writer.println("                                {");
                    writer.println("                                    toastr.error(retorno.message, \"Erro!\", opts);");
                    writer.println("                                }");
                    writer.println("                            }");
                    writer.println("                        });");
                    writer.println("                    },");
                    writer.println("                    error: function(retorno)");
                    writer.println("                    {");
                    writer.println("                        show_loading_bar({");
                    writer.println("                            pct: 100,");
                    writer.println("                            finish: function(pct)");
                    writer.println("                            {");
                    writer.println("                                toastr.error(\"Ocorreu um erro interno!\", \"Erro\", opts);");
                    writer.println("                            }");
                    writer.println("                        });");
                    writer.println("                    }");
                    writer.println("                });");
                    writer.println("            }");
                    writer.println("        });");
                    
                    writer.println("        $(\".botaoEditar\").click(function()");
                    writer.println("        {");
                    writer.println("");
                    writer.println("            var id = $(this).attr(\"id\");");
                    writer.println("");
                    writer.println("            $('#edit').modal('show',");
                    writer.println("            {");
                    writer.println("                backdrop: 'static'");
                    writer.println("            });");
                    writer.println("");
                    writer.println("            show_loading_bar(65);");
                    writer.println("");
                    writer.println("            $.ajax({");
                    writer.println("                url: '<?=BASE_URL.CONTROLLER?>/r-" + tabelas.get(i) + "',");
                    writer.println("                type: 'POST',");
                    writer.println("                data:");
                    writer.println("                {");
                    writer.println("                    id: id");
                    writer.println("                },");
                    writer.println("                dataType: \"json\",");
                    writer.println("                beforeSend: function() {},");
                    writer.println("                complete: function() {},");
                    writer.println("                success: function(retorno)");
                    writer.println("                {");
                    writer.println("                    show_loading_bar({");
                    writer.println("                        pct: 100,");
                    writer.println("                        finish: function(pct)");
                    writer.println("                        {");
                    writer.println("                            if(retorno.status)");
                    writer.println("                            {");
                    
                    for (int k = 1; k <= count; k++)
                    {
                        if(!metaData.getColumnLabel(k).contains("id_"))
                        {
                            writer.println("                                $(\"#" + metaData.getColumnLabel(k) + "\").val(retorno." + metaData.getColumnLabel(k) + ");");
                        }
                        else
                        {
                            writer.println("                                $(\"select#" + metaData.getColumnLabel(k) + "\").val(retorno." + metaData.getColumnLabel(k) + ");");
                        }
                    }
                    
                    writer.println("                                    $(\"#loading\").fadeOut(\"slow\", function()");
                    writer.println("                                    {");
                    writer.println("                                        $(\"#dados\").fadeIn(\"fast\");");
                    writer.println("                                    });");
                    writer.println("                            }");
                    writer.println("                            else");
                    writer.println("                            {");
                    writer.println("                                toastr.error(retorno.message, \"Erro!\", opts);");
                    writer.println("                                $('#edit').modal('hide', ");
                    writer.println("                                {");
                    writer.println("                                    backdrop: 'static'");
                    writer.println("                                 });");
                    writer.println("                            }");
                    writer.println("                        }");
                    writer.println("                    });");
                    writer.println("                },");
                    writer.println("                error: function(retorno) ");
                    writer.println("                {");
                    writer.println("                    show_loading_bar({ pct: 100, finish: function(pct) {");
                    writer.println("                        toastr.error(\"Ocorreu um erro interno!\", \"Erro\", opts);");
                    writer.println("                            $('#edit').modal('hide', ");
                    writer.println("                            {");
                    writer.println("                                backdrop: 'static'");
                    writer.println("                            });");
                    writer.println("                        }");
                    writer.println("                    });");
                    writer.println("                }");
                    writer.println("            });");
                    writer.println("        });");
                    
                    writer.println("");
                    writer.println("        $(\"#botaoSalvar\").click(function()");
                    writer.println("        {");
                    writer.println("");
                    writer.println("            show_loading_bar(60);");
                    writer.println("");
                    writer.println("            ");
                    
                    for (int k = 1; k <= count; k++)
                    {
                        writer.println("        var " + metaData.getColumnLabel(k) + " = $(\"#" + metaData.getColumnLabel(k) + "\").val();");
                    }
                    
                    for (int k = 1; k <= count; k++)
                    {
                        writer.println("");
                        writer.println("            if(" + metaData.getColumnLabel(k) + " === \"\")");
                        writer.println("            {");
                        writer.println("                toastr.error(\"O campo " + metaData.getColumnLabel(k) +" é obrigatório\", \"Campo necessário!\", opts);");
                        writer.println("                $(\"#grau\").focus();");
                        writer.println("                return;");
                        writer.println("            }");
                        writer.println("");
                    }
                    
                    writer.println("            show_loading_bar(80);");
                    writer.println("");
                    writer.println("            $.ajax({");
                    writer.println("                url: '<?=BASE_URL.CONTROLLER?>/u-" + tabelas.get(i) + "',");
                    writer.println("                type: 'POST',");
                    writer.println("                data:");
                    writer.println("                {");
                    
                    for (int k = 1; k <= count; k++)
                    {
                        writer.println("                    " + metaData.getColumnLabel(k) + ": " + metaData.getColumnLabel(k) + ",");
                    }
                    writer.println("                },");
                    writer.println("                dataType: \"json\",");
                    writer.println("                beforeSend: function() {},");
                    writer.println("                complete: function() {},");
                    writer.println("                success: function(retorno)");
                    writer.println("                {");
                    writer.println("                    show_loading_bar({");
                    writer.println("                        pct: 100,");
                    writer.println("                        finish: function(pct)");
                    writer.println("                        {");
                    writer.println("                            if(retorno.status)");
                    writer.println("                            {");
                    writer.println("                                toastr.success(\"Suas alterações foram salvas com sucesso!\", \"Feito!\", opts);");
                    writer.println("                            }");
                    writer.println("                            else");
                    writer.println("                            {");
                    writer.println("                                toastr.error(retorno.message, \"Erro\", opts);");
                    writer.println("                            }");
                    writer.println("                        }");
                    writer.println("                    });");
                    writer.println("                },");
                    writer.println("                error: function(retorno)");
                    writer.println("                {");
                    writer.println("                    show_loading_bar({");
                    writer.println("                        pct: 100,");
                    writer.println("                        finish: function(pct)");
                    writer.println("                        {");
                    writer.println("                            toastr.error(\"Ocorreu um erro interno!\", \"Erro\", opts);");
                    writer.println("                        }");
                    writer.println("                    });");
                    writer.println("                }");
                    writer.println("            });");
                    writer.println("        });");                   
                    writer.println("    });");
                    writer.println("    </script>");
                    writer.println("</body>");
                    writer.println("<link href='http://fonts.googleapis.com/css?family=Open+Sans+Condensed:300' rel='stylesheet' type='text/css'>");
                    writer.println("<script src=\"<?=BASE_URL?>public/assets/js/toastr.js\"></script>");
                    
                    writer.close();
                    System.out.println("View criada com sucesso!");
                    
                    writer = new PrintWriter(diretorioMenu + "cadastrar-" + tabelas.get(i) + ".phtml", "UTF-8");
                    writer.println("<?php");
                    writer.println("");
                    writer.println("    $page = $this->page;");
                    writer.println("    $" + tabelas.get(i) + " = $this->" + tabelas.get(i) + ";");
                    writer.println("    $usuario = $this->usuario;");
                    
                    listaDeModels = "";
                    for(int n = 0; n < tabelas.size(); n++)
                    {
                        sString = tabelas.get(n).toString().toLowerCase();
                        sString = Character.toString(sString.charAt(0)).toUpperCase() + sString.substring(1);

                        partes = sString.split("_");

                        novaString = "";
                        for(int j = 0; j < partes.length; j++)
                        {
                            temp = partes[j].toLowerCase();
                            temp = Character.toString(temp.charAt(0)).toUpperCase() + temp.substring(1);
                            novaString += temp;
                        }

                        listaDeModels += "$model" + novaString + " = new Application_Model_DbTable_" + novaString + ";\n";
                    }
                    
                    writer.println("    " + listaDeModels);
                    
                    sString = tabelas.get(i).toString().toLowerCase();
                    partes = sString.split("_");
                    novaString = "";
                    for(int j = 0; j < partes.length; j++)
                    {
                        temp = partes[j].toLowerCase();
                        temp = Character.toString(temp.charAt(0)).toUpperCase()+temp.substring(1);
                        novaString += temp;
                    }

                    writer.println("?>");
                    writer.println("");
                    writer.println("<style>");
                    writer.println(".page-container.horizontal-menu header.navbar.navbar-fixed-top { z-index: 100; }");
                    writer.println("</style>");
                    writer.println("");
                    writer.println("<body class=\"page-body page-fade\" data-url=\"http://www.yourapp.com.br\">");
                    writer.println("    <div class=\"page-container horizontal-menu\">");
                    writer.println("        <header class=\"navbar navbar-fixed-top\">");
                    writer.println("            <div class=\"navbar-inner\">");
                    writer.println("                <ul class=\"navbar-nav\">");
                    writer.println("                    <?php echo $this->render(CONTROLLER . '/includes/menu.phtml'); ?>");
                    writer.println("                </ul>");
                    writer.println("            </div>");
                    writer.println("        </header>");
                    writer.println("    <div class=\"main-content\">");
                    writer.println("        <div class=\"container\">");
                    writer.println("            <div class=\"row\">");
                    writer.println("                <div class=\"col-md-6 col-sm-8 clearfix\">");
                    writer.println("                    <ul class=\"user-info pull-left pull-none-xsm\">");
                    writer.println("                        <li class=\"profile-info dropdown\">");
                    writer.println("                            <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">");
                    writer.println("                                <img src=\"http://app.hireme.com.br/uploads/nophoto.jpg\" alt=\"\" class=\"img-circle\" width=\"44\" /><?= $usuario->nome ?>");
                    writer.println("                            </a>");
                    writer.println("                            <ul class=\"dropdown-menu\">");
                    writer.println("                                <li class=\"caret\"></li>");
                    writer.println("                                <li>");
                    writer.println("                                    <a href=\"#\">");                    
                    writer.println("                                        <i class=\"entypo-user\"></i>");  
                    writer.println("                                        Editar sua conta");  
                    writer.println("                                    </a>");  
                    writer.println("                                </li>");  
                    writer.println("                            </ul>");  
                    writer.println("                        </li>");  
                    writer.println("                    </ul>");  
                    writer.println("                    <ul class=\"user-info pull-left pull-right-xs pull-none-xsm\">");  
                    writer.println("                    </ul>");  
                    writer.println("                </div>");  
                    writer.println("                <div class=\"col-md-6 col-sm-4 clearfix hidden-xs\">");  
                    writer.println("                    <ul class=\"list-inline links-list pull-right\">");  
                    writer.println("                        <li class=\"sep\"></li>");  
                    writer.println("                        <li class=\"sep\"></li>");
                    writer.println("                        <li>");
                    writer.println("                            <a href=\"<?= BASE_URL . CONTROLLER ?>/sair\">");
                    writer.println("                                Sair do Sistema <i class=\"entypo-logout right\"></i>");
                    writer.println("                            </a>");
                    writer.println("                        </li>");
                    writer.println("                    </ul>");
                    writer.println("                </div>");
                    writer.println("            </div>");
                    writer.println("            <h3 style=\"font-family: 'Open Sans Condensed', sans-serif; font-size: 32px;\">Cadastrar " + novaString + "</h3>");
                    writer.println("            <br />");
                    
                    writer.println("            <div class=\"row\">");
                    writer.println("                <div class=\"col-md-12\">");
                    writer.println("                    <div class=\"panel panel-primary\" data-collapsed=\"0\">");
                    writer.println("                        <div class=\"panel-heading\">");
                    writer.println("                            <div class=\"panel-title\">");
                    writer.println("                                Cadastrar");
                    writer.println("                            </div>");
                    writer.println("                            <div class=\"panel-options\">");
                    writer.println("                                <a href=\"#\" data-rel=\"collapse\"><i class=\"entypo-down-open\"></i></a>");
                    writer.println("                            </div>");
                    writer.println("                        </div>");
                    writer.println("                        <div class=\"panel-body\">");
                    writer.println("                            <form role=\"form\" class=\"form-horizontal form-groups-bordered\">");
                    
                    for (int k = 1; k <= count; k++)
                    {
                        if(metaData.getColumnLabel(k).contains("id_"))
                        {
                            String nomeTabela = metaData.getColumnLabel(k).replace("id_", "");
                            String titulo = Character.toString(nomeTabela.charAt(0)).toUpperCase() + nomeTabela.substring(1);
                            
                            String sStringTemp = nomeTabela.toLowerCase();
                            sStringTemp = Character.toString(sStringTemp.charAt(0)).toUpperCase() + sStringTemp.substring(1);

                            String[] partesTemp = sStringTemp.split("_");

                            String novaStringTemp = "";
                            for(int j = 0; j < partesTemp.length; j++)
                            {
                                String tempMomento = partesTemp[j].toLowerCase();
                                tempMomento = Character.toString(tempMomento.charAt(0)).toUpperCase() + tempMomento.substring(1);
                                novaStringTemp += tempMomento;
                            }
                                    
                            writer.println("                        <div class=\"form-group\">");
                            writer.println("                            <label class=\"col-sm-3 control-label\" for=\"field-1\">" + titulo + "</label>");
                            writer.println("                            <div class=\"col-sm-5\">");
                            writer.println("                                <select id=\"" + metaData.getColumnLabel(k) + "\" class=\"select2\" data-allow-clear=\"true\" data-placeholder=\"Selecione...\">");
                            writer.println("                                    <?php");
                            writer.println("");
                            writer.println("                                    $" + metaData.getColumnLabel(k).replace("id_", "") + "s = $model" + novaStringTemp + "s->fetchAll();");
                            writer.println("");
                            writer.println("                                    if(sizeof($" + metaData.getColumnLabel(k).replace("id_", "") + "s) > 0)");
                            writer.println("                                    {");
                            writer.println("                                        foreach($" + metaData.getColumnLabel(k).replace("id_", "") + "s as $temp)");
                            writer.println("                                        {");
                            writer.println("                                        ?>");
                            writer.println("                                            <option value=\"<?=$temp->id?>\"><?=$temp->nome?></option>");
                            writer.println("                                        <?php");
                            writer.println("                                        }");
                            writer.println("                                    }");
                            writer.println("                                    ?>");
                            writer.println("                                </select>");
                            writer.println("                            </div>");
                            writer.println("                        </div>");
                        }
                        else
                        {
                            if(!metaData.getColumnLabel(k).equals("id"))
                            {
                                if(!metaData.getColumnLabel(k).equals("data"))
                                {
                                    writer.println("                        <div class=\"form-group\">");
                                    writer.println("                            <label class=\"col-sm-3 control-label\" for=\"field-1\">" + metaData.getColumnLabel(k).replace("_", " ") + "</label>");
                                    writer.println("                            <div class=\"col-sm-9\">");
                                    writer.println("                                <input type=\"text\" placeholder=\"Digitar o " + metaData.getColumnLabel(k) + "\" id=\"" + metaData.getColumnLabel(k) + "\" class=\"form-control\">");
                                    writer.println("                            </div>");
                                    writer.println("                        </div>");
                                }
                                else
                                {
                                    writer.println("                        <div class=\"form-group\">");
                                    writer.println("                            <label class=\"col-sm-3 control-label\" for=\"field-1\">" + metaData.getColumnLabel(k).replace("_", " ") + "</label>");
                                    writer.println("                            <div style=\"padding:0; margin:0;\"class=\"input-group col-sm-3\">");
                                    writer.println("                                <input id=\"data\" type=\"text\" class=\"form-control datepicker\" data-format=\"D, yyyy MM dd\">");
                                    writer.println("                                <div class=\"input-group-addon\">");
                                    writer.println("                                    <a href=\"#\"><i class=\"entypo-calendar\"></i></a>");
                                    writer.println("                                </div>");
                                    writer.println("                            </div>");
                                    writer.println("                        </div>");
                                }
                            }
                        }
                    }
                    
                    writer.println("                                <div class=\"form-group\">");
                    writer.println("                                    <div class=\"col-sm-offset-3 col-sm-5\">");
                    writer.println("                                        <button id=\"botaoCadastrar\" type=\"button\" class=\"btn btn-info\">Cadastrar</button>");
                    writer.println("                                    </div>");
                    writer.println("                                </div>");
                    writer.println("                            </form>");
                    writer.println("                        </div>");
                    writer.println("                    </div>");
                    writer.println("                </div>");
                    writer.println("            </div>");
                    
                    writer.println("            <footer class=\"main\">");
                    writer.println("                &copy; 2015 YourApp Inc. by <a href=\"http://www.yourapp.com.br\" target=\"_blank\">YourApp</a>");
                    writer.println("            </footer>");
                    writer.println("        </div>");
                    writer.println("    </div>");
                    writer.println("</div>");
                    writer.println("");
                    
                    writer.println("    <script type=\"text/javascript\">");
                    writer.println("    $(function()");
                    writer.println("    {");
                    writer.println("");
                    writer.println("        var opts = { \"closeButton\": true, \"debug\": false, \"positionClass\": \"toast-top-right\", \"onclick\": null, \"showDuration\": \"300\", \"hideDuration\": \"1000\", \"timeOut\": \"5000\", \"extendedTimeOut\": \"1000\", \"showEasing\": \"swing\", \"hideEasing\": \"linear\", \"showMethod\": \"fadeIn\", \"hideMethod\": \"fadeOut\"};");
                    writer.println("");
                    writer.println("        $(\"#botaoCadastrar\").click(function()");
                    writer.println("        {");
                    writer.println("");
                    writer.println("            show_loading_bar(60);");
                    writer.println("");
                    writer.println("            ");
                    
                    for (int k = 1; k <= count; k++)
                    {
                        writer.println("        var " + metaData.getColumnLabel(k) + " = $(\"#" + metaData.getColumnLabel(k) + "\").val();");
                    }
                    
                    for (int k = 1; k <= count; k++)
                    {
                        writer.println("");
                        writer.println("            if(" + metaData.getColumnLabel(k) + " === \"\")");
                        writer.println("            {");
                        writer.println("                toastr.error(\"O campo " + metaData.getColumnLabel(k) +" é obrigatório\", \"Campo necessário!\", opts);");
                        writer.println("                $(\"#" + metaData.getColumnLabel(k) + "\").focus();");
                        writer.println("                return;");
                        writer.println("            }");
                        writer.println("");
                    }
                    
                    writer.println("            show_loading_bar(80);");
                    writer.println("");
                    writer.println("            $.ajax({");
                    writer.println("                url: '<?=BASE_URL.CONTROLLER?>/c-" + tabelas.get(i) + "',");
                    writer.println("                type: 'POST',");
                    writer.println("                data:");
                    writer.println("                {");
                    
                    for (int k = 1; k <= count; k++)
                    {
                        writer.println("                    " + metaData.getColumnLabel(k) + ": " + metaData.getColumnLabel(k) + ",");
                    }
                    writer.println("                },");
                    writer.println("                dataType: \"json\",");
                    writer.println("                beforeSend: function() {},");
                    writer.println("                complete: function() {},");
                    writer.println("                success: function(retorno)");
                    writer.println("                {");
                    writer.println("                    show_loading_bar({");
                    writer.println("                        pct: 100,");
                    writer.println("                        finish: function(pct)");
                    writer.println("                        {");
                    writer.println("                            if(retorno.status)");
                    writer.println("                            {");
                    writer.println("                                toastr.success(\"Cadastro realizado com sucesso!\", \"Feito!\", opts);");
                    writer.println("                                location.href='<?=BASE_URL.CONTROLLER?>/" + tabelas.get(i) + "'");
                    writer.println("                            }");
                    writer.println("                            else");
                    writer.println("                            {");
                    writer.println("                                toastr.error(retorno.message, \"Erro\", opts);");
                    writer.println("                            }");
                    writer.println("                        }");
                    writer.println("                    });");
                    writer.println("                },");
                    writer.println("                error: function(retorno)");
                    writer.println("                {");
                    writer.println("                    show_loading_bar({");
                    writer.println("                        pct: 100,");
                    writer.println("                        finish: function(pct)");
                    writer.println("                        {");
                    writer.println("                            toastr.error(\"Ocorreu um erro interno!\", \"Erro\", opts);");
                    writer.println("                        }");
                    writer.println("                    });");
                    writer.println("                }");
                    writer.println("            });");
                    writer.println("        });");                   
                    writer.println("    });");
                    writer.println("    </script>");
                    
                    writer.println("");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/jcrop/jquery.Jcrop.min.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/datatables/responsive/css/datatables.responsive.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/select2/select2-bootstrap.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/select2/select2.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/selectboxit/jquery.selectBoxIt.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/daterangepicker/daterangepicker-bs3.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/icheck/skins/minimal/_all.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/icheck/skins/square/_all.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/icheck/skins/flat/_all.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/icheck/skins/futurico/futurico.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/icheck/skins/polaris/polaris.css\">");
                    writer.println("    <link rel=\"stylesheet\" href=\"<?= BASE_URL ?>public/assets/js/vertical-timeline/css/component.css\">");
                    writer.println("");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/bootstrap-datepicker.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/bootstrap-timepicker.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/bootstrap-colorpicker.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/gsap/main-gsap.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/jquery-ui/js/jquery-ui-1.10.3.minimal.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/bootstrap.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/joinable.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/resizeable.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/neon-api.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/jquery.dataTables.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/datatables/TableTools.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/dataTables.bootstrap.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/datatables/jquery.dataTables.columnFilter.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/datatables/lodash.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/datatables/responsive/js/datatables.responsive.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/select2/select2.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/neon-chat.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/jcrop/jquery.Jcrop.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/neon-custom.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/neon-demo.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/gsap/main-gsap.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/jquery-ui/js/jquery-ui-1.10.3.minimal.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/bootstrap.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/joinable.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/resizeable.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/neon-api.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/neon-custom.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/neon-demo.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/daterangepicker/moment.min.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/daterangepicker/daterangepicker.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/jquery.multi-select.js\"></script>");
                    writer.println("    <script src=\"<?= BASE_URL ?>public/assets/js/icheck/icheck.min.js\"></script>");
                    writer.println("");
                    writer.println("    <link href='http://fonts.googleapis.com/css?family=Open+Sans+Condensed:300' rel='stylesheet' type='text/css'>");
                    writer.println("    <script src=\"<?=BASE_URL?>public/assets/js/toastr.js\"></script>");
                    
                    writer.println("</body>");
                    
                    writer.close();
                }
            }
        }
        catch(FileNotFoundException | UnsupportedEncodingException | SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
