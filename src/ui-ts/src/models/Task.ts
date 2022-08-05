import { Item } from "./Item"

export interface Task {
    serialVersionUID: number
    aufgabenHash: number
    aufgabenStellung: string
    aufgabenInhalt: string
    niveau: string
    frontEndType: string
    items: Item[]
}