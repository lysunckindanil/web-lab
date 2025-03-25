function deleteIssue(issueId) {
    let token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/forum/issue/delete',
        headers: {'X-CSRF-TOKEN': token},
        method: 'post',
        dataType: 'json',
        data: {
            'issueId': issueId
        },
        success: function () {
            document.getElementById('issue' + issueId).remove()
        }
    });
}

function deleteIssueIndex(issueId) {
    let token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/forum/issue/delete',
        headers: {'X-CSRF-TOKEN': token},
        method: 'post',
        dataType: 'json',
        data: {
            'issueId': issueId
        },
        success: function () {
            window.location.href = '/forum'
        }
    });
}

function deleteComment(commentId) {
    let token = $("meta[name='_csrf']").attr("content")
    $.ajax({
        url: '/forum/comment/delete',
        headers: {'X-CSRF-TOKEN': token},
        method: 'post',
        dataType: 'json',
        data: {
            'commentId': commentId
        },
        success: function () {
            document.getElementById('comment' + commentId).remove()
        }
    });
}

function createComment1(issueId) {
    let token = $("meta[name='_csrf']").attr("content")
    let content = document.getElementById("textareaComment").value
    $.ajax({
        url: '/forum/comment/create',
        headers: {'X-CSRF-TOKEN': token},
        method: 'post',
        dataType: 'json',
        data: {
            'issueId': issueId,
            'content': content
        },
        success: function () {
            location.reload()
        },
        error: function (data) {
            let error = document.getElementById('createCommentError')
            error.textContent = data['responseJSON']['error']
            error.hidden = false
        }
    });
}
