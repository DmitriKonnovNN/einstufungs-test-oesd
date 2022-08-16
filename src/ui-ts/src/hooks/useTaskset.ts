import {useEffect, useState} from "react";
import {TaskSet} from "../models/Taskset";
import axios,  {AxiosError} from "axios";

export function useTaskset () {


    const [taskset, setTaskset] = useState<TaskSet>();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('')

    async function fetchTaskset (){
        try {
            setError('')
            setLoading(true)
            const response = await axios.get<TaskSet>("http://localhost:8080/api/v2.0.0/et_ufzgi")
            setTaskset(response.data)
            setLoading(false)
        }
        catch (e: unknown) {
            const error = e as AxiosError
            setLoading(false)
            setError(error.message)
        }
    }

    useEffect(()=>{fetchTaskset()},[])

    return {taskset, loading, error}
}