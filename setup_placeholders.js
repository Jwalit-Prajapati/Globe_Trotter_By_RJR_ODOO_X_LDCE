import fs from 'fs';
import path from 'path';

const pages = [
  'Login', 'Signup', 'Dashboard', 'CreateTrip', 'TripList', 
  'ItineraryBuilder', 'ItineraryView', 'CitySearch', 'ActivitySearch', 
  'TripBudget', 'TripCalendar', 'SharedItinerary', 'Profile', 'AdminDashboard'
];

const dir = path.join(process.cwd(), 'src', 'pages');
if (!fs.existsSync(dir)){
    fs.mkdirSync(dir, { recursive: true });
}

pages.forEach(page => {
  const content = `export default function ${page}() {
  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold">${page}</h1>
      <p className="mt-4 text-gray-600">Placeholder for ${page} screen</p>
    </div>
  );
}
`;
  fs.writeFileSync(path.join(dir, `${page}.jsx`), content);
});

console.log('Placeholder pages created.');
