import React from "react";
import NounAggregator from "./NounAggregator";

function Report(props) {
  const data = props.data;
  console.log(data);
  console.log(props.type);

  //display the data in a table

  const columns = data.length > 0 ? Object.keys(data[0]) : [];
  if (data === undefined || data.length === 0) {
    return <div></div>;
  } else if (props.type !== "14") {
    return (
      <div className="flex flex-col gap-10 mt-5 p-2">
        <table className="border-collapse border border-gray-200">
          <thead>
            <tr className="bg-gray-200">
              {columns.map((column, index) => (
                <th key={index} className="border border-gray-200 px-4 py-2">
                  {column}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {data.map((item, rowIndex) => (
              <tr key={rowIndex}>
                {columns.map((column, colIndex) => (
                  <td
                    key={colIndex}
                    className="border border-gray-200 px-4 py-2"
                  >
                    {item[column]}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  } else if (props.type === "14") {
    return (
      <div className="flex flex-col gap-10 mt-5 p-2">
        <NounAggregator jsonData={data} />;
      </div>
    );
  }
}

export default Report;
