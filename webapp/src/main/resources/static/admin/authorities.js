function grantRedactor(username) {
    let token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/admin/grantRedactor',
        headers: {'X-CSRF-TOKEN': token},
        method: 'post',
        dataType: 'json',
        data: {
            'username': username
        },
        success: function () {
            location.reload()
        }
    });
}

function revokeRedactor(username) {
    let token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/admin/revokeRedactor',
        headers: {'X-CSRF-TOKEN': token},
        method: 'post',
        dataType: 'json',
        data: {
            'username': username
        },
        success: function () {
            location.reload()
        }
    });
}