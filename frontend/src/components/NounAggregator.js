import React, { useEffect, useState } from 'react';
import nlp from 'compromise';

const CommonNounsAggregator = ({ jsonData }) => {
  const [commonNouns, setCommonNouns] = useState([]);

  useEffect(() => {
    // Function to extract common nouns from the JSON value
    const getCommonNouns = (jsonValue) => {
      const nouns = {};

      Object.values(jsonValue).forEach((text) => {
        const doc = nlp(text.content);
        const nounsInText = doc.match('#Noun').out('array');
        
        nounsInText.forEach((noun) => {
          const cleanedNoun = noun.toLowerCase();
          nouns[cleanedNoun] = (nouns[cleanedNoun] || 0) + 1;
        });
      });

      return nouns;
    };

    const aggregatedNouns = getCommonNouns(jsonData);
    setCommonNouns(aggregatedNouns);
  }, [jsonData]);

  return (
    <div>
      <table className="table-auto w-full">
        <thead>
          <tr className="bg-gray-200 border-gray-500">
            <th className="px-4 py-2">Noun</th>
            <th className="px-4 py-2">Count</th>
          </tr>
        </thead>
        <tbody>
          {Object.entries(commonNouns).map(([noun, count], index) => (
            <tr key={index}>
              <td className="border px-4 py-2">{noun}</td>
              <td className="border px-4 py-2">{count}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default CommonNounsAggregator;
