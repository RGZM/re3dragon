let API = {};

API.BASE = "http://localhost:8084/re3dragon/rest";

API.getDragonItemData = (callback, uri, spinner, target) => {
    setTimeout(function() {
        $.ajax({
            type: 'GET',
            async: false,
            url: API.BASE + "/item?uri=" + uri,
            error: function(jqXHR, textStatus, errorThrown) {
                console.error(errorThrown);
            },
            success: function(response) {
                try {
                    response = JSON.parse(response);
                } catch (e) {}
                if (typeof callback === 'function') {
                    if (spinner != null && target != null) {
                        callback(spinner, target, response);
                    } else {
                        callback(response);
                    }
                } else {
                    return response;
                }
            }
        });
    }, 0);
};

API.getDragonItemDataSearch = (callback, q, repo, spinner, target) => {
    setTimeout(function() {
        $.ajax({
            type: 'GET',
            async: false,
            url: API.BASE + "/search?q=" + q + "&repo=" + repo,
            error: function(jqXHR, textStatus, errorThrown) {
                console.error(errorThrown);
            },
            success: function(response) {
                try {
                    response = JSON.parse(response);
                } catch (e) {}
                if (typeof callback === 'function') {
                    if (spinner != null && target != null) {
                        callback(spinner, target, response);
                    } else {
                        callback(response, q, repo);
                    }
                } else {
                    return response;
                }
            }
        });
    }, 0);
};

API.getDragonItemDataLookUp = (callback, ids, spinner, target) => {
    setTimeout(function() {
        $.ajax({
            type: 'GET',
            async: false,
            url: API.BASE + "/items?ids=" + ids,
            error: function(jqXHR, textStatus, errorThrown) {
                console.error(errorThrown);
            },
            success: function(response) {
                try {
                    response = JSON.parse(response);
                } catch (e) {}
                if (typeof callback === 'function') {
                    if (spinner != null && target != null) {
                        callback(spinner, target, response);
                    } else {
                        callback(response, "", "");
                    }
                } else {
                    return response;
                }
            }
        });
    }, 0);
};