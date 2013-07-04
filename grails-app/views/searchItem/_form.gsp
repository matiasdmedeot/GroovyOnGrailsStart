<%@ page import="mlaparel.SearchItem" %>



<div class="fieldcontain ${hasErrors(bean: searchItemInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="searchItem.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${searchItemInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: searchItemInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="searchItem.title.label" default="Title" />
		
	</label>
	<g:textField name="title" value="${searchItemInstance?.title}"/>
</div>

