// src/services/sdnApi.js
const BACKEND_URL = "http://10.214.61.63:8082";

export async function fetchTrafficData() {
    try {
        const response = await fetch(`${BACKEND_URL}/api/v1/network/traffic`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error("API Error fetching traffic:", error);
        throw error;
    }
}