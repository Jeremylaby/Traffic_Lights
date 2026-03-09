import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css'
import {PreviewPage} from "@/pages";

function App() {
    return (
        <BrowserRouter>
        <Routes>
            <Route path="/preview" element={<PreviewPage />} />
        </Routes>
        </BrowserRouter>)
}

export default App
