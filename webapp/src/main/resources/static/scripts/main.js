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
    let token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/forum/comment/delete',
        headers: {'X-CSRF-TOKEN': token},
        method: 'post',
        dataType: 'json',
        data: {
            'commentId': commentId,

        },
        success: function () {
            document.getElementById('comment' + commentId).remove()
        }
    });
}

