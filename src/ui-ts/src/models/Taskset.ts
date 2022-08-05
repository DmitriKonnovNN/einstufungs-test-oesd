import { Task } from "./Task"

export interface TaskSet {
    serialVersionUID: number
    aufgabenBogenId: number
    createdAt: number
    aufgabenListe: Task[]
}