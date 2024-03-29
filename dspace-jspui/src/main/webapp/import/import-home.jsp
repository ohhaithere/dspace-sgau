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
    <form action="/jspui/import-item" method="post" name="edit_metadata" id="edit_metadata" onkeydown="return disableEnterKey(event);">
  <span class="col-md-5">
    <input class="form-control" type="text" name="uuid_search" placeholder="Идентификатор" size="23" value=""/>
  </span>
        <input type="hidden" name="collection_id" value="<%=collection_id %>" />
        <input type="hidden" name="community_id" value="<%=community_id %>" />
        <br>
        <br>
        <input class="btn btn-primary pull-left col-md-3" type="submit" name="submit" value="Найти">
    </form>

    <br>
    <br>
    <b>Поиск по имени и заголовку</b><br>
    <form action="/jspui/import-item" method="post" name="edit_metadata" id="edit_metadata" onkeydown="return disableEnterKey(event);">
  <span class="col-md-5">
    <input class="form-control" type="text" name="name" placeholder="Имя автора" size="23" value=""/>
  </span>
  <span class="col-md-5">
    <input class="form-control" type="text" name="title" placeholder="Наименование" size="23" value=""/>
  </span>
        <input type="hidden" name="collection_id" value="<%=collection_id %>" />
        <input type="hidden" name="community_id" value="<%=community_id %>" />
        <input type="hidden" name="action" value="search" />
        <br>
        <br>
        <input class="btn btn-primary pull-left col-md-3" type="submit" name="submit" value="Найти">
    </form>


</dspace:layout>