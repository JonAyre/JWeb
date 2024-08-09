function showToast(id)
{
    let toast = $('#' + id);
    toast.toast("show");
}

function makeToast(containerId, toastId, title, message, delay)
{
    let container = document.getElementById(containerId);
    let toast = document.createElement('div');
    toast.className = 'toast hide';
    toast.id = toastId;
    toast.setAttribute('role', 'alert');
    toast.setAttribute('aria-live', 'assertive');
    toast.setAttribute('aria-atomic', 'true');
    toast.setAttribute('data-bs-delay', delay.toString());
    if (title.trim().length === 0)
    {
        toast.innerHTML =
            '<div class="toast-body text-muted">' +
            '    <span class="fs-4">' + message + '</span>' +
            '    <button type="button" class="btn-close float-end" data-bs-dismiss="toast"></button>' +
            '</div>';
    }
    else
    {
        toast.innerHTML =
            '<div class="toast-header">' +
            '    <strong class="me-auto">' + title + '</strong>' +
            '    <button type="button" class="btn-close" data-bs-dismiss="toast"></button>' +
            '</div>' +
            '<div class="toast-body text-muted">' +
            '    <span class="text-xxl-center">' + message + '</span>' +
            '</div>';
    }

    container.appendChild(toast);
}