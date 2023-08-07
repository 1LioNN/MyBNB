import React from "react";

function Report(props) {
  const data = props.data;
  console.log(data);

  //display the data in a table

  const columns = data.length > 0 ? Object.keys(data[0]) : [];

  return (
    <div className="flex flex-col gap-10 mt-5 p-2">
      <table className="border-collapse border border-black">
        <thead>
          <tr>
            {columns.map((column, index) => (
              <th key={index} className="border border-black px-4 py-2">
                {column}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((item, rowIndex) => (
            <tr key={rowIndex}>
              {columns.map((column, colIndex) => (
                <td key={colIndex} className="border border-black px-4 py-2">
                  {item[column]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Report;
