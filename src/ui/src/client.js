import fetch from 'unfetch'

const checkStatus = response => {
    if(response.ok) {
        return response;
    }
    const error = new Error(response.statusText)
    error.response = response
    return Promise.reject(error)
}

export const sumbitSolutionsSet = solset =>
    fetch("api/v2.0.0/et_ufzgi",{
        headers: {'Content-Type':'application/json'},
        method: 'POST',
        body: JSON.stringify(solset)
    }).then(checkStatus);

export const fetchTaskSet = () =>
    fetch("api/v2.0.0/et_ufzgi")
        .then(checkStatus);