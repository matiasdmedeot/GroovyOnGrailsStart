package mlaparel

import grails.plugins.rest.client.RestBuilder
import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.dao.DataIntegrityViolationException

class SearchItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
		def rest = new RestBuilder()
		def resp = rest.get("https://api.mercadolibre.com/countries")
	    def countries = resp.json
		
		params.max = Math.min(max ?: 10, 100)
        [searchItemInstanceList: SearchItem.list(params), searchItemInstanceTotal: SearchItem.count()]
    }

    def create() {
        [searchItemInstance: new SearchItem(params)]
    }

    def save() {
        def searchItemInstance = new SearchItem(params)
        if (!searchItemInstance.save(flush: true)) {
            render(view: "create", model: [searchItemInstance: searchItemInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), searchItemInstance.id])
        redirect(action: "show", id: searchItemInstance.id)
    }

    def show(Long id) {
        def searchItemInstance = SearchItem.get(id)
        if (!searchItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "list")
            return
        }

        [searchItemInstance: searchItemInstance]
    }

    def edit(Long id) {
        def searchItemInstance = SearchItem.get(id)
        if (!searchItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "list")
            return
        }

        [searchItemInstance: searchItemInstance]
    }

    def update(Long id, Long version) {
        def searchItemInstance = SearchItem.get(id)
        if (!searchItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (searchItemInstance.version > version) {
                searchItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'searchItem.label', default: 'SearchItem')] as Object[],
                          "Another user has updated this SearchItem while you were editing")
                render(view: "edit", model: [searchItemInstance: searchItemInstance])
                return
            }
        }

        searchItemInstance.properties = params

        if (!searchItemInstance.save(flush: true)) {
            render(view: "edit", model: [searchItemInstance: searchItemInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), searchItemInstance.id])
        redirect(action: "show", id: searchItemInstance.id)
    }

    def delete(Long id) {
        def searchItemInstance = SearchItem.get(id)
        if (!searchItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "list")
            return
        }

        try {
            searchItemInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "show", id: id)
        }
    }
}
