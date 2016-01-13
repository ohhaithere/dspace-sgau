<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Profile editing page
  -
  - Attributes to pass in:
  -
  -   eperson          - the EPerson who's editing their profile
  -   missing.fields   - if a Boolean true, the user hasn't entered enough
  -                      information on the form during a previous attempt
  -   password.problem - if a Boolean true, there's a problem with password
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
           prefix="fmt" %>


<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.eperson.EPerson, org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.core.Utils" %>
<%@ page import="org.dspace.storage.rdbms.TableRowIterator" %>
<%@ page import="org.dspace.storage.rdbms.TableRow" %>

<%

    String collection_id = (String) request.getAttribute("collection_id");
    String community_id = (String) request.getAttribute("community_id");
%>

<dspace:layout style="submission" titlekey="jsp.register.edit-profile.title" nocache="true">
    <b>Поиск по идентификатору</b><br>
    <form action="/jspui/import-mass" enctype="multipart/form-data" method="post" name="edit_metadata" id="edit_metadata" onkeydown="return disableEnterKey(event);">
  <span class="col-md-5">
    <input class="form-control" type="file" name="file" placeholder="Файл" size="23" value=""/>
  </span>
        <br>
        <br>
        <input class="btn btn-primary pull-left col-md-3" type="submit" name="submit" value="Найти">
    </form>


</dspace:layout>