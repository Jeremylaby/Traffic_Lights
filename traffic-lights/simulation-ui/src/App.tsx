import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css'
import {CrossroadPreview, PreviewPage, RoadPreviewPage} from "@/pages";

function App() {
    return (
        <BrowserRouter>
        <Routes>
            <Route path="/preview" element={<PreviewPage />} />
            <Route path={"/preview/road"} element={<RoadPreviewPage/>} />
            <Route path={"/preview/crossroad"} element={<CrossroadPreview/>} />
        </Routes>
        </BrowserRouter>)
}

export default App
