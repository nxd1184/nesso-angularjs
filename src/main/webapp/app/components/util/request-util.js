if (!window.LA) {
    window.LA = {};
}
LA.RequestUtils = {
    getHeaders: function (contentType) {
        var requestContentType = 'application/json';
        if (contentType == 'multipart/form-data') {
            requestContentType = undefined;
        }

        var token = '';
        if(localStorage.getItem('jhi-authenticationToken')) {
            token = localStorage.getItem('jhi-authenticationToken').replace(new RegExp('"', 'g'), '');
        } else if (sessionStorage.getItem('jhi-authenticationToken')) {
            token = sessionStorage.getItem('jhi-authenticationToken').replace(new RegExp('"', 'g'), '');
        }


        return {
            'Content-Type': requestContentType,
            Authorization: 'Bearer ' + token
        }
    },

    request: function (method, url, data) {
        return {
            method: method,
            url: url,
            headers: this.getHeaders(),
            data: data
        }
    },

    get: function (url, data, cache) {
        return {
            method: 'GET',
            url: url,
            headers: this.getHeaders(),
            data: data,
            cache: !!cache
        }
    },

    post: function (url, data) {
        return {
            method: 'POST',
            url: url,
            headers: this.getHeaders(),
            data: data
        }
    },

    put: function (url, data) {
        return {
            method: 'PUT',
            url: url,
            headers: this.getHeaders(),
            data: data
        }
    },

    delete: function (url, data) {
        return {
            method: 'DELETE',
            url: url,
            headers: this.getHeaders(),
            data: data
        }
    }

};
