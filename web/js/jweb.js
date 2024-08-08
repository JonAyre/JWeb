function showToast(id)
{
    let toast = $('#' + id);
    toast.toast("show");
}

function makeToast(containerId, toastId, title, message)
{
    let container = $('#' + containerId);
    container.innerHTML =
        '<div id="' + toastId + '" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="3000">\n' +
        '    <div class="toast-header">\n' +
        '        <strong class="me-auto">' + title + '</strong>\n' +
        '        <button type="button" class="btn-close" data-bs-dismiss="toast"></button>\n' +
        '    </div>\n' +
        '    <div class="toast-body text-muted">\n' +
        '        <h2>' + message + '</h2>\n' +
        '    </div>\n' +
        '</div>\n';
    alert(container.innerHTML);
}