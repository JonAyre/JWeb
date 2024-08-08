function showToast(id)
{
    let toast = $('#' + id);
    toast.toast("show");
}

function makeToast(containerId, toastId, title, message)
{
    let container = document.getElementById(containerId);
    let toast = document.createElement('div');
    toast.className = 'toast hide';
    toast.id = toastId;
    toast.setAttribute('role', 'alert');
    toast.setAttribute('aria-live', 'assertive');
    toast.setAttribute('aria-atomic', 'true');
    toast.setAttribute('data-bs-delay', '3000');
    toast.innerHTML =
        '<div class="toast-header">\n' +
        '    <strong class="me-auto">' + title + '</strong>\n' +
        '    <button type="button" class="btn-close" data-bs-dismiss="toast"></button>\n' +
        '</div>\n' +
        '<div class="toast-body text-muted">\n' +
        '    <h2>' + message + '</h2>\n' +
        '</div>\n';
    container.appendChild(toast);
}