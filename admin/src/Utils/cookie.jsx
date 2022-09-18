function get(key) {
    let cookies = document.cookie;
    let array = cookies
        .split(';')
        .filter(str => {
            let arr = str.trim().split('=');
            return arr[0] == key;
        });
    if (!array || array.length <= 0) {
        return '';
    }
    return decodeURIComponent(array[0].trim().split('=')[1]);
}

function set(name, value, time = null) {
    let cookie = `${name}=${encodeURIComponent(value)}`;
    let expires = (time) ? `; expires=${time.toUTCString()}` : '';
    let path = `; path =/`;
    document.cookie = (`${cookie}${expires}${path}`);
}

function remove(key) {
    document.cookie = `${key}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`
}
export {
    get, set, remove
}