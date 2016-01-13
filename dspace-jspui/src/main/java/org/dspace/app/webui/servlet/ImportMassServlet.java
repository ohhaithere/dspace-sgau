package org.dspace.app.webui.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.dspace.content.MetadataSchema;
import org.dspace.content.WorkspaceItem;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.dspace.handle.HandleManager;
import org.dspace.identifier.Handle;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by root on 1/12/16.
 */

public class ImportMassServlet extends DSpaceServlet {

    private Item it;


    protected void doDSGet(Context context, HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("/import/import-mass-home.jsp").forward(request, response);
    }

    protected void doDSPost(Context context, HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException{

        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    // ... (do your job here)
                } else {
                    // Process form file field (input type="file").
                    String fieldName = item.getFieldName();
                    String fileName = FilenameUtils.getName(item.getName());
                    InputStream fileContent = item.getInputStream();
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(fileContent, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String inline = "";
                    while ((inline = inputReader.readLine()) != null) {
                        sb.append(inline);
                    }
                    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(sb.toString()));

                    Document doc = db.parse(is);
                    NodeList records = doc.getElementsByTagName("Records");
                    for(int i = 0; i < records.getLength(); i++){
                        Element record = (Element) records.item(i);

                        Collection col = Collection.find(context, 5);
                        WorkspaceItem wsitem = WorkspaceItem.create(context, col, false);
                        Item itemItem = wsitem.getItem();
                        response.getWriter().write("test");

                        itemItem.setOwningCollection(col);
                        Element titleNode = (Element) record.getElementsByTagName("Title").item(0);
                        Node tex = titleNode.getElementsByTagName("Value").item(0);
                        itemItem.addMetadata(MetadataSchema.DC_SCHEMA, "title", null, "ru", tex.getTextContent());

                        try{
                            Node author = record.getElementsByTagName("Creator").item(0);
                            String authorName = author.getTextContent();
                            itemItem.addMetadata(MetadataSchema.DC_SCHEMA, "contributor", "author", "ru", author.getTextContent());
                        } catch(Exception e){
                            response.getWriter().write(e.getMessage());
                        }

                        itemItem.setDiscoverable(true);
                        HandleManager.createHandle(context, itemItem);

                       // Group groups = Group.findByName(context, "Anonymous");
                        TableRow row = DatabaseManager.row("collection2item");

                        response.setCharacterEncoding("UTF-8");
                        request.setCharacterEncoding("UTF-8");

                        row.setColumn("collection_id", col.getID());
                        row.setColumn("item_id", itemItem.getID());

                        DatabaseManager.insert(context, row);

                        itemItem.inheritCollectionDefaultPolicies(col);

                        itemItem.setArchived(true);

                        itemItem.update();
                        context.complete();
                        break;

                    }

                }
            }
        } catch (FileUploadException e) {
            response.getWriter().write("file upload");
        }  catch (ParserConfigurationException e) {
            response.getWriter().write("parser");
        } catch (SAXException e) {
            response.getWriter().write(e.getMessage());
        }


    }

}
