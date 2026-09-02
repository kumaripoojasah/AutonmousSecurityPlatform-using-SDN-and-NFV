// src/services/sdnApi.js
const BACKEND_URL = "http://localhost:9090";

export async function fetchTrafficData() {
    try {
        const token = localStorage.getItem("sdn_token");
        const response = await fetch(`${BACKEND_URL}/api/traffic/live`, {
            headers: token ? { Authorization: `Bearer ${token}` } : {},
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error("API Error fetching traffic:", error);
        throw error;
    }
}